package com.ityu.common.utils

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * Spring工具类
 * Created by macro on 2020/3/3.
 */
@Component
open class SpringContextHolder : ApplicationContextAware {
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        if (Companion.applicationContext == null) {
            Companion.applicationContext = applicationContext
        }
    }

    companion object {
        // 获取applicationContext
        open var applicationContext: ApplicationContext? = null
            private set

        // 通过name获取Bean
        fun getBean(name: String?): Any {
            return applicationContext!!.getBean(name!!)
        }

        // 通过class获取Bean
        fun <T> getBean(clazz: Class<T>): T {
            return applicationContext!!.getBean(clazz)
        }

        // 通过name,以及Clazz返回指定的Bean
        fun <T> getBean(name: String?, clazz: Class<T>): T {
            return applicationContext!!.getBean(name!!, clazz)
        }
    }
}
