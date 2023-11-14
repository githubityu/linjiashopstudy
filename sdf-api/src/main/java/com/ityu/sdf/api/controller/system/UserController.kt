package com.ityu.sdf.api.controller.system

import com.ityu.common.bean.constant.Const
import com.ityu.common.bean.constant.factory.PageFactory
import com.ityu.common.bean.constant.state.ManagerStatus
import com.ityu.common.bean.core.BussinessLog
import com.ityu.common.bean.entity.system.User
import com.ityu.common.bean.vo.front.Rets.success
import com.ityu.common.bean.vo.query.SearchFilter
import com.ityu.common.core.factory.UserFactory
import com.ityu.common.dto.UserDto
import com.ityu.common.exception.Asserts
import com.ityu.common.exception.ResultCode
import com.ityu.common.service.system.UserService
import com.ityu.common.utils.BeanUtil
import com.ityu.common.utils.CryptUtil
import com.ityu.common.utils.factory.Page
import com.ityu.common.warpper.UserWrapper
import com.ityu.sdf.api.utils.ApiConstants
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

/**
 * UserController
 *
 * @author enilu
 * @version 2018/9/15 0015
 */
@Api(tags = ["user"])
@Tag(name = "user", description = "用户模块")
@RestController
@RequestMapping("/user")
open class UserController(
    private var passwordEncoder: PasswordEncoder,
    private var userService: UserService,

    ) {
    @GetMapping(value = ["/list"])
    open fun list(
        @RequestParam(required = false) account: String?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) deptid: Long?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) status: Int?,
        @RequestParam(required = false) sex: Int?
    ): Any {
        var page: Page<User?> = PageFactory<User>().defaultPage()
        page.addFilter("name", SearchFilter.Operator.LIKE, name)
        page.addFilter("account", SearchFilter.Operator.LIKE, account)
        page.addFilter("deptid", deptid)
        page.addFilter("phone", phone)
        page.addFilter("status", status)
        page.addFilter("status", SearchFilter.Operator.GT, 0)
        page.addFilter("sex", sex)
        page = userService.queryPage(page)
        val list = UserWrapper(BeanUtil.objectsToMaps(page.records)).warp() as List<User>
        page.setRecords(list)
        return success(page)
    }

    @BussinessLog(value = "重置密码", key = "userId")
    @PostMapping(value = ["resetPassword"])
    open fun resetPassword(userId: Long): Any {
        val user = userService.get(userId)
        user!!.password = passwordEncoder.encode("111111")
        userService.update(user)
        return success()
    }

    @PostMapping
    @BussinessLog(value = "编辑账号", key = "name")
    open fun save(@Valid @RequestBody user: UserDto): Any {
        if (user.id == null) {
            user.let {
                val theUser: User? = userService.findByAccountForLogin(it.account)
                if (theUser != null) {
                    Asserts.fail(ResultCode.USER_ALREADY_REG)
                }
                it.password = passwordEncoder.encode(CryptUtil.desEncrypt(user.password))
                it.status = ManagerStatus.OK.code
                userService.insert(UserFactory.createUser(user, User()))
            }

        } else {
            val oldUser: User = userService.get(user.id)!!
            userService.update(UserFactory.updateUser(user, oldUser))
        }
        return success()
    }


    @BussinessLog(value = "删除账号", key = "userId")
    @DeleteMapping
    open fun remove(@RequestParam userId: Long?): Any {
        if (userId == null) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        if (userId!!.toInt() <= 3) {
            Asserts.fail("不能删除初始用户")
        }
        val user = userService[userId]
        user!!.status = ManagerStatus.DELETED.code
        userService.update(user)
        return success()
    }

    @BussinessLog(value = "设置账号角色", key = "userId")
    @PostMapping(value = ["/setRole"])
    open fun setRole(@RequestParam("userId") userId: Long, @RequestParam("roleIds") roleIds: String?): Any {
        if (BeanUtil.isOneEmpty(userId, roleIds)) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        //不能修改超级管理员
        if (userId.toInt() == Const.ADMIN_ID) {
            Asserts.fail("不能修改超级管理员得角色")
        }
        val user = userService[userId]
        user!!.roleid = roleIds
        userService.update(user)
        return success()
    }

    @BussinessLog(value = "冻结/解冻账号", key = "userId")
    @GetMapping(value = ["changeStatus"])
    open fun changeStatus(@RequestParam userId: Long?): Any {
        if (userId == null) {
            Asserts.fail(ResultCode.REQUEST_NULL)
        }
        if (userId!!.toInt() <= 3) {
            Asserts.fail("不能冻结初始用户")
        }
        val user = userService[userId]
        user!!.status = (
                if (user!!.status === ManagerStatus.OK.code
                ) ManagerStatus.FREEZED.code else ManagerStatus.OK.code
                )
        userService.update(user)
        return success()
    }


}
