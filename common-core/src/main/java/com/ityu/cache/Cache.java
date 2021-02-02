package com.ityu.cache;

/**
 * 所有缓存名称的集合
 *
 * @author fengshuonan
 * @date 2017-04-24 21:56
 */
public interface Cache {

    /**
     * 将数据库中的数据加载到缓存中
     */
    void cache();


    /**
     * 获取缓存数据
     *
     * @param key
     * @return
     */
    String get(String key);


    /**
     * 设置缓存数据
     *
     * @param key
     * @param val
     */
    void set(String key, Object val);
}
