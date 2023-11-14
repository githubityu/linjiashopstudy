package com.ityu.common.cache

import com.ityu.common.bean.core.AdminUser
import com.ityu.common.utils.HttpUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * 用户登录时，生成的Token与用户ID的对应关系
 */
@Service
open class TokenCache(private val cacheDao: CacheDao) {
    private val logger = LoggerFactory.getLogger(javaClass)

    open fun put(token: String?, idUser: Long?) {
        cacheDao!!.hset(CacheDao.SESSION, token, idUser)
    }

    open fun get(token: String?): Long? {
        return cacheDao!!.hget(CacheDao.SESSION, token, Long::class.java)
    }

    open val idUser: Long?
        get() = cacheDao!!.hget(CacheDao.SESSION, HttpUtil.getToken(), Long::class.java)

    open fun remove(token: String) {
        cacheDao!!.hdel(CacheDao.SESSION, token + "user")
    }

    open fun setUser(token: String, adminUser: AdminUser?) {
        cacheDao!!.hset(CacheDao.SESSION, token + "user", adminUser)
    }

    open fun getUser(token: String): AdminUser? {
        return cacheDao!!.hget(CacheDao.SESSION, token + "user", AdminUser::class.java)
    }
}
