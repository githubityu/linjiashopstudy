package com.ityu.common.service.system

import com.ityu.common.bean.core.AdminUser
import com.ityu.common.bean.entity.system.User
import com.ityu.common.bean.vo.node.RouterMenu
import com.ityu.common.cache.CacheDao
import com.ityu.common.cache.TokenCache
import com.ityu.common.dao.system.RoleRepository
import com.ityu.common.dao.system.UserRepository
import com.ityu.common.exception.ApiException
import com.ityu.common.service.BaseService
import com.ityu.common.service.system.impl.ConstantFactory
import com.ityu.common.utils.Convert
import com.ityu.common.utils.JwtTokenUtil
import com.ityu.common.utils.StringUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created  on 2018/3/23 0023.
 *
 * @author enilu
 */
@Service
open class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val menuRepository: MenuService,
    private val tokenCache: TokenCache,
    private val cacheDao: CacheDao,
    private val jwtTokenUtil: JwtTokenUtil
) : BaseService<User?, Long?, UserRepository?>() {
    private val logger = LoggerFactory.getLogger(UserService::class.java)


    @Value("\${jwt.expiration}")
    private val expiration: Long? = null
    open fun loadUserByUsername(account: String?): Pair<User, Set<RouterMenu>> {
        val user = findByAccountForLogin(account)
        if (user != null) {
            return getPariUserDetail(user)
        }
        throw ApiException("用户名或密码错误")
    }

    open fun loadUserByPhone(phone: String?): Pair<User, Set<RouterMenu>> {
        val user = findByPhone(phone)
        if (user != null) {
            return getPariUserDetail(user)
        }
        throw ApiException("用户名或密码错误")
    }

    private fun getPariUserDetail(user: User): Pair<User, Set<RouterMenu>> {
        val roleArray: Array<String?> = user.roleid!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val menusByRoleids = menuRepository.getAllMenus(Arrays.stream(roleArray).toList())
        val permissions: MutableSet<RouterMenu> = HashSet()
        permissions.addAll(menusByRoleids)
        return Pair<User, Set<RouterMenu>>(user, permissions)
    }


    open fun setAdminUser(token: String?, user: User): AdminUser {
        var adminUser = tokenCache.getUser(token?:"")
        if (adminUser != null) {
            return adminUser
        }
        adminUser = AdminUser()
        adminUser.id = user.id // 账号id
        adminUser.account = user.account // 账号
        adminUser.deptId = user.deptid // 部门id
        adminUser.deptName = ConstantFactory.me().getDeptName(user.deptid) // 部门名称
        adminUser.name = user.name // 用户名称
        adminUser.password = user.password
        val roleArray = Convert.toLongArray(",", user.roleid)
        val roleList: MutableList<Long> = ArrayList()
        val roleNameList: MutableList<String> = ArrayList()
        val roleCodeList: MutableList<String> = ArrayList()
        val permissions: MutableSet<String> = HashSet()
        val resUrls: MutableSet<String> = HashSet()
        for (roleId in roleArray) {
            roleList.add(roleId)
            val role = roleRepository.getOne(roleId)
            roleNameList.add(role.name!!)
            roleCodeList.add(role.code!!)
            permissions.addAll(menuRepository.getResCodesByRoleId(roleId))
            val list = menuRepository.getResUrlsByRoleId(roleId)
            for (resUrl in list) {
                if (StringUtil.isNotEmpty(resUrl)) {
                    resUrls.add(resUrl)
                }
            }
        }
        adminUser.roleList = roleList
        adminUser.roleNames = roleNameList
        adminUser.roleCodes = roleCodeList
        adminUser.permissions = permissions
        adminUser.urls = resUrls
        tokenCache.setUser(token?:"", adminUser)
        return adminUser
    }

    /**
     * 登录查询用户的时候不从缓存中查询
     *
     * @param account
     * @return
     */
    open fun findByAccountForLogin(account: String?): User? {
        val user = userRepository.findByAccount(account)
        cacheDao.hset(CacheDao.SESSION, account, user)
        return user
    }

    open fun findByAccount(account: String?): User? {
        val user = cacheDao.hget(CacheDao.SESSION, account, User::class.java)
        return user ?: findByAccountForLogin(account)
    }

    open fun findByPhone(phone: String?): User? {
        val user = userRepository.findByPhone(phone)
        cacheDao.hset(CacheDao.SESSION, phone, user)
        return user
    }

    override fun update(record: User?): User {
        val user = super.update(record)!!
        cacheDao.hset(CacheDao.SESSION, user.account, user)
        return user
    }

    /**
     * 根据用户信息生成token
     *
     * @param user
     * @return
     */
    open fun loginForToken(user: User): String {
        //获取用户token值
        val token = jwtTokenUtil.generateToken(user.account, user.id)
        //将token作为RefreshToken Key 存到缓存中，缓存时间为token有效期的两倍
        val expireDate = Date(System.currentTimeMillis() + expiration!! * 2 * 1000)
        cacheDao.hset(CacheDao.SESSION, token, expireDate.time.toString())
        return token
    }

    /**
     * 获取refreshToken是否有效
     *
     * @param token
     * @return
     */
    open fun refreshTokenIsValid(token: String?): Boolean {
        val refreshTokenTime = cacheDao.hget(CacheDao.SESSION, token) as String ?: return false
        return System.currentTimeMillis() <= refreshTokenTime.toLong()
    }
}
