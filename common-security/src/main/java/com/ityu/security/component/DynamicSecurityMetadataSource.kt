package com.ityu.security.component

import cn.hutool.core.util.URLUtil
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource
import org.springframework.util.AntPathMatcher
import org.springframework.util.PathMatcher

/**
 * 动态权限数据源，用于获取动态权限规则
 * Created by macro on 2020/2/7.
 */
open class DynamicSecurityMetadataSource : FilterInvocationSecurityMetadataSource {
    @Autowired
    private val dynamicSecurityService: DynamicSecurityService? = null
    @PostConstruct
    fun loadDataSource() {
        configAttributeMap = dynamicSecurityService!!.loadDataSource()
    }

    fun clearDataSource() {
        configAttributeMap!!.clear()
        configAttributeMap = null
    }

    @Throws(IllegalArgumentException::class)
    override fun getAttributes(o: Any): Collection<ConfigAttribute> {
        //获取当前访问的路径
        val url = (o as FilterInvocation).requestUrl
        val path = URLUtil.getPath(url)
        return getConfigAttributesWithPath(path)
    }

    //根据当前访问的路径获取对应权限
    fun getConfigAttributesWithPath(path: String?): List<ConfigAttribute> {
        if (configAttributeMap == null) loadDataSource()
        val configAttributes: MutableList<ConfigAttribute> = ArrayList()
        val pathMatcher: PathMatcher = AntPathMatcher()
        val iterator: Iterator<String> = configAttributeMap!!.keys.iterator()
        //获取访问该路径所需资源
        while (iterator.hasNext()) {
            val pattern = iterator.next()
            if (pathMatcher.match(pattern, path!!)) {
                configAttributes.add(configAttributeMap!![pattern]!!)
            }
        }
        // 未设置操作请求权限，返回空集合
        return configAttributes
    }

    override fun getAllConfigAttributes(): Collection<ConfigAttribute>? {
        return null
    }

    override fun supports(aClass: Class<*>?): Boolean {
        return true
    }

    companion object {
        private var configAttributeMap: MutableMap<String, ConfigAttribute>? = null
    }
}
