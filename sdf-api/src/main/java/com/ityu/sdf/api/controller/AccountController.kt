package com.ityu.sdf.api.controller

import com.ityu.common.bean.entity.system.User
import com.ityu.common.bean.vo.front.Ret
import com.ityu.common.bean.vo.front.Rets
import com.ityu.common.bean.vo.front.Rets.failure
import com.ityu.common.bean.vo.node.RouterMenu
import com.ityu.common.cache.CacheDao
import com.ityu.common.cache.TokenCache
import com.ityu.common.core.log.LogManager
import com.ityu.common.core.log.LogTaskFactory
import com.ityu.common.dto.LoginDto
import com.ityu.common.exception.Asserts
import com.ityu.common.exception.ResultCode
import com.ityu.common.service.system.MenuService
import com.ityu.common.service.system.QrcodeService
import com.ityu.common.service.system.UserService
import com.ityu.common.utils.*
import com.ityu.common.utils.HttpUtil.getToken
import com.ityu.sdf.api.bo.AdminUserDetails
import com.ityu.sdf.api.utils.ApiConstants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

/**
 * AccountController
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@Api(tags = ["account"])
@Tag(name = "account", description = "账户模块")
@RestController
@RequestMapping("/account")
open class AccountController(
    private var userService: UserService,
    private var menuService: MenuService,
    private var passwordEncoder: PasswordEncoder,
    private var jwtTokenUtil: JwtTokenUtil,
    private var tokenCache: TokenCache,
    private var cacheDao: CacheDao,
    private var qrcodeService: QrcodeService,
) {
    private val logger = LoggerFactory.getLogger(AccountController::class.java)

    /**
     * 用户登录<br></br>
     * 1，验证没有注册<br></br>
     * 2，验证密码错误<br></br>
     * 3，登录成功
     *
     * @param loginDto
     * @return
     */
    @PostMapping(value = ["/login"])
    @SecurityRequirements
    fun login(@RequestBody loginDto: LoginDto?): Ret<*> {
        var token: String?
        //密码需要客户端加密后传递
        //密码需要客户端加密后传递
        try {
            val result = userService.loadUserByUsername(loginDto!!.username)
            val userDetails = AdminUserDetails(result.first, result.second)
            val password = CryptUtil.desEncrypt(loginDto.password)
            if (!passwordEncoder.matches(password, userDetails.password)) {
                Asserts.fail("密码不正确")
            }
            if (!userDetails.isEnabled) {
                Asserts.fail("帐号已被禁用")
            }
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
            token = jwtTokenUtil.generateToken(userDetails.username, result.first.id)
            LogManager.me().executeLog(LogTaskFactory.loginLog(userDetails.umsAdmin.id, HttpUtil.getIp()))
            val map = mapOf(
                "token" to token
            )
            userService.setAdminUser(token, result.first)
            return Rets.success(map)
        } catch (e: AuthenticationException) {
            logger.warn("登录异常:{}", e.message)
        }
        return failure("登录时失败", null)
    }

    @GetMapping(value = ["/info"])
    fun info(): Any {
        var id: Long? = null
        try {
            id = HttpUtil.getUserId(jwtTokenUtil)
        } catch (e: Exception) {
            return Rets.expire()
        }
        if (id != null) {
            var user: User? = userService.get(id) ?: //该用户可能被删除
            return Rets.expire()
            if (StringUtil.isEmpty(user!!.roleid)) {
                return failure("该用户未配置权限")
            }
            val shiroUser = tokenCache.getUser(jwtTokenUtil.getTokenRemoveHead(getToken()))

            if (shiroUser == null) {
                Asserts.fail(ResultCode.UNAUTHORIZED)
            }
            val list: List<RouterMenu> = menuService.getSideBarMenus(shiroUser!!.roleList)

            val result = mapOf(
                "roles" to shiroUser!!.roleCodes,
                "username" to user.account,
                "name" to user.name,
                "menus" to list,
                "permissions" to shiroUser.urls,
                "profile" to mapOf(
                    "dept" to shiroUser.deptName,
                    "roles" to shiroUser.roleNames,
                ),
            )
            return Rets.success(result)
        }
        return failure("获取用户信息失败")
    }

    @PostMapping(value = ["/updatePwd"])
    fun updatePwd(oldPassword: String?, password: String, rePassword: String): Any {
        try {
            if (StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)) {
                return failure("密码不能为空")
            }
            if (password != rePassword) {
                return failure("新密码前后不一致")
            }
            val user: User? = userService.findByAccountForLogin(HttpUtil.getUsername(jwtTokenUtil))
            if (ApiConstants.ADMIN_ACCOUNT == user?.account) {
                return failure("不能修改超级管理员密码")
            }
            val rawPassword = CryptUtil.desEncrypt(oldPassword)
            if (!passwordEncoder.matches(rawPassword, user?.password)) {
                Asserts.fail("旧密码输入错误")
            }

            user?.password = passwordEncoder.encode(password)
            userService.update(user)
            return Rets.success()
        } catch (e: java.lang.Exception) {
            logger.error(e.message, e)
        }
        return failure("更改密码失败")
    }

    /**
     * @param phone 用户账号
     * @param qrcode  二维码值
     * @param confirm 是否确认登录：1:是,0:否
     * @return
     */
    @PostMapping("/qrcode/login")
    fun qrLogin(
        @RequestParam("phone") phone: String?,
        @RequestParam("qrcode") qrcode: String?,
        @RequestParam("confirm") confirm: String?
    ): Ret<*> {
        val qrstatus: String = qrcodeService.getCrcodeStatus(qrcode)
        if (QrcodeService.INVALID == qrstatus) {
            return failure("二维码已过期")
        }
        if (QrcodeService.SUCCESS == qrstatus || QrcodeService.CANCEL == qrstatus) {
            return failure("二维码已被他人使用")
        }
        return if (QrcodeService.UNDO == qrstatus) {
            if ("0" == confirm) {
                cacheDao.hset(CacheDao.SHORT, qrcode, QrcodeService.CANCEL)
                failure("已取消")
            }
            val result = userService.loadUserByPhone(phone)
            val userDetails = AdminUserDetails(result.first, result.second)
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication

            val token = userService.loginForToken(result.first)

            userService.setAdminUser(token, result.first)
            cacheDao.hset(
                CacheDao.SHORT, qrcode, JsonUtil.toJson(
                    Maps.newHashMap(
                        "status", QrcodeService.SUCCESS, "account", result.first.account, "token", token
                    )
                )
            )
            Rets.success()
        } else if (QrcodeService.INVALID == qrstatus) {
            failure("二维码已过期")
        } else {
            failure("无效的二维码")
        }
    }

    /**
     * 获取二维码扫码结果(PC端调用）
     *
     * @return
     */
    @GetMapping("/qrcode/getRet")
    fun getQrcodeStatus(@RequestParam("uuid") uuid: String?): Ret<*> {
        val ret = qrcodeService.getCrcodeStatus(uuid)
        if (QrcodeService.INVALID == ret) {
            return Rets.success(Maps.newHashMap("status", ret, "msg", "二维码已过期"))
        }
        if (QrcodeService.CANCEL == ret) {
            return Rets.success(Maps.newHashMap("status", ret, "msg", "已取消登录"))
        }
        if (QrcodeService.UNDO == ret) {
            return Rets.success(Maps.newHashMap("status", ret, "msg", "待扫描"))
        }
        val map: Map<*, *> = JsonUtil.fromJson(MutableMap::class.java, ret)
        return Rets.success(map)
    }


    @ApiOperation(value = "登出功能")
    @RequestMapping(value = ["/logout"], method = [RequestMethod.POST])
    @ResponseBody
    fun logout(): Ret<*> {
        tokenCache.remove(getToken())
        return Rets.success(null)
    }

}
