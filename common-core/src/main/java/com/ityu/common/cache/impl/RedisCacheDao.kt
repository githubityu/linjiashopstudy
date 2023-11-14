package com.ityu.common.cache.impl

import com.ityu.common.cache.CacheDao
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.io.Serializable

/**
 * Redis缓存实现类<br></br>
 * 请不要直接使用该类，而是使用其接口CacheDao，以方便实际应用中往其他缓存服务切换（比如redis,ssdb等)
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@Component
open class RedisCacheDao(private val cacheManager: CacheManager) : CacheDao {

    override fun hset(key: Serializable, k: Serializable?, `val`: Any?) {
        val cache = cacheManager.getCache(key.toString())
        cache!!.put(k?:"", `val`)
    }

    override fun hget(key: Serializable, k: Serializable?): Serializable {
        val cache = cacheManager.getCache(key.toString())
        return cache!!.get(k?:"", String::class.java)!!
    }

    override fun <T> hget(key: Serializable, k: Serializable?, klass: Class<T>): T? {
        val cache = cacheManager.getCache(key.toString())
        try {
            return cache!!.get(k?:"", klass)
        } catch (e: Exception) {
            return null
        }
    }

    override fun set(key: Serializable, `val`: Any) {
        val cache = cacheManager.getCache(CacheDao.CONSTANT)
        cache!!.put(key, `val`)
    }

    override fun <T> get(key: Serializable, klass: Class<T>): T? {
        return cacheManager.getCache(CacheDao.CONSTANT)!!.get(key.toString(), klass)
    }

    override fun get(key: Serializable): String {
        return cacheManager.getCache(CacheDao.CONSTANT)!!.get(key.toString(), String::class.java)!!
    }

    override fun del(key: Serializable) {
        cacheManager.getCache(CacheDao.CONSTANT)!!.put(key.toString(), null)
    }

    override fun hdel(key: Serializable, k: Serializable) {
        cacheManager.getCache(key.toString())!!.put(k.toString(), null)
    }
}
