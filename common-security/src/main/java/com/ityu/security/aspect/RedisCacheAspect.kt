package com.ityu.security.aspect

import com.ityu.security.annotation.CacheException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Redis缓存切面，防止Redis宕机影响正常业务逻辑
 * Created by macro on 2020/3/17.
 */
@Aspect
@Component
@Order(2)
open class RedisCacheAspect {
    @Pointcut("execution(public * com.macro.mall.portal.service.*CacheService.*(..)) || execution(public * com.macro.mall.service.*CacheService.*(..))")
    fun cacheAspect() {
    }

    @Around("cacheAspect()")
    @Throws(Throwable::class)
    open fun doAround(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature
        val methodSignature = signature as MethodSignature
        val method = methodSignature.method
        var result: Any? = null
        try {
            result = joinPoint.proceed()
        } catch (throwable: Throwable) {
            //有CacheException注解的方法需要抛出异常
            if (method.isAnnotationPresent(CacheException::class.java)) {
                throw throwable
            } else {
                LOGGER.error(throwable.message)
            }
        }
        return result
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RedisCacheAspect::class.java)
    }
}
