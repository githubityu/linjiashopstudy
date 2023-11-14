package com.ityu.sdf.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * 全局跨域配置
 * Created by macro on 2019/7/27.
 */
@Configuration
open class GlobalCorsConfig {
    /**
     * 允许跨域调用的过滤器
     */
    @Bean
    open fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        //允许所有域名进行跨域调用
        config.addAllowedOriginPattern("*")
        //允许跨越发送cookie
        config.allowCredentials = true
        //放行全部原始头信息
        config.addAllowedHeader("*")
        //允许所有请求方法跨域调用
        config.addAllowedMethod("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}
