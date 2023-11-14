package com.ityu.security.component

import org.springframework.security.access.ConfigAttribute

/**
 * 动态权限相关业务接口
 * Created by macro on 2020/2/7.
 */
interface DynamicSecurityService {
    /**
     * 加载资源ANT通配符和资源对应MAP
     */
    fun loadDataSource(): MutableMap<String, ConfigAttribute>?
}
