package com.ityu.security.annotation

/**
 * 自定义缓存异常注解，有该注解的缓存方法会抛出异常
 * Created by macro on 2020/3/17.
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class CacheException
