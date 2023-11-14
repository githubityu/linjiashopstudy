package com.ityu.security.config

import com.ityu.common.utils.JwtTokenUtil
import com.ityu.security.component.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

/**
 * SpringSecurity通用配置
 * 包括通用Bean、Security通用Bean及动态权限通用Bean
 * Created by macro on 2022/5/20.
 */
@Configuration
open class CommonSecurityConfig {
    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun ignoreUrlsConfig(): IgnoreUrlsConfig {
        return IgnoreUrlsConfig()
    }

    @Bean
    open fun jwtTokenUtil(): JwtTokenUtil {
        return JwtTokenUtil()
    }

    @Bean
    open fun restfulAccessDeniedHandler(): RestfulAccessDeniedHandler {
        return RestfulAccessDeniedHandler()
    }

    @Bean
    open fun restAuthenticationEntryPoint(): RestAuthenticationEntryPoint {
        return RestAuthenticationEntryPoint()
    }

    @Bean
    open fun jwtAuthenticationTokenFilter(): JwtAuthenticationTokenFilter {
        return JwtAuthenticationTokenFilter()
    }

    @Bean
    open fun dynamicSecurityMetadataSource(): DynamicSecurityMetadataSource {
        return DynamicSecurityMetadataSource()
    }

    @Bean
    open fun dynamicAuthorizationManager(): DynamicAuthorizationManager {
        return DynamicAuthorizationManager()
    }
    @Bean
    open fun mvc(introspector: HandlerMappingIntrospector?): MvcRequestMatcher.Builder {
        return MvcRequestMatcher.Builder(introspector)
    }
}
