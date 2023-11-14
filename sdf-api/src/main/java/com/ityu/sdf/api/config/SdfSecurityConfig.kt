package com.ityu.sdf.api.config

import com.ityu.common.service.system.MenuService
import com.ityu.common.service.system.UserService
import com.ityu.sdf.api.bo.AdminUserDetails
import com.ityu.security.component.DynamicSecurityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.access.SecurityConfig
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.concurrent.ConcurrentHashMap

/**
 * mall-security模块相关配置
 * Created by macro on 2019/11/9.
 * http://localhost:8087/swagger-ui/index.html
 */
@Configuration
open class SdfSecurityConfig(private val adminService: UserService, private val resourceService: MenuService) {
    @Bean
    open fun userDetailsService(): UserDetailsService {
        //获取登录用户信息
        return UserDetailsService { username: String ->
            println("username$username")
            val (first, second) = adminService.loadUserByUsername(username)
            AdminUserDetails(first, second)
        }
    }

    @Bean
    open fun dynamicSecurityService(): DynamicSecurityService {
        return object : DynamicSecurityService {
            override fun loadDataSource(): MutableMap<String, ConfigAttribute>? {
                val map: MutableMap<String, ConfigAttribute> = ConcurrentHashMap()
                val resourceList = resourceService.queryAll()
                for (resource in resourceList) {
                    map[resource.url!!] = SecurityConfig(resource.id.toString() + ":" + resource.code)
                }
                return map
            }
        }
    }
}
