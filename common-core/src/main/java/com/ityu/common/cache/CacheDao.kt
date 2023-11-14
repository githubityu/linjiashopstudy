package com.ityu.common.cache

import java.io.Serializable

/**
 * 缓存接口
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
interface CacheDao {
    /**
     * 设置hash key值
     *
     * @param key
     * @param k
     * @param val
     */
    open fun hset(key: Serializable, k: Serializable?, `val`: Any?)

    /**
     * 获取hash key值
     *
     * @param key
     * @param k
     * @return
     */
    open fun hget(key: Serializable, k: Serializable?): Serializable?

    /**
     * 获取hash key值
     *
     * @param key
     * @param k
     * @param klass
     * @param <T>
     * @return
    </T> */
    open fun <T> hget(key: Serializable, k: Serializable?, klass: Class<T>): T?

    /**
     * 设置key值，超时失效
     *
     * @param key
     * @param val
     */
    open fun set(key: Serializable, `val`: Any)

    /**
     * 获取key值
     *
     * @param key
     * @param klass
     * @return
     */
    open fun <T> get(key: Serializable, klass: Class<T>): T?
    open fun get(key: Serializable): String?
    open fun del(key: Serializable)
    open fun hdel(key: Serializable, k: Serializable)

    companion object {
        const val CONSTANT = "CONSTANT"
        const val SESSION = "SESSION"
        const val SHORT = "SHORT"
    }
}
