package com.ityu.security.config

import com.ityu.security.component.DynamicAuthorizationManager
import com.ityu.security.component.JwtAuthenticationTokenFilter
import com.ityu.security.component.RestAuthenticationEntryPoint
import com.ityu.security.component.RestfulAccessDeniedHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher


/**
 * SpringSecurity 6.x以上新用法配置
 * 为避免循环依赖，仅用于配置HttpSecurity
 * Created by macro on 2019/11/5.
 */
@Configuration
@EnableWebSecurity
open class SecurityConfig {

    @Autowired
    private val restfulAccessDeniedHandler: RestfulAccessDeniedHandler? = null

    @Autowired
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint? = null

    @Autowired
    private val jwtAuthenticationTokenFilter: JwtAuthenticationTokenFilter? = null

    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Autowired
    private val ignoreUrlsConfig: IgnoreUrlsConfig? = null

    @Autowired
    private val dynamicAuthorizationManager: DynamicAuthorizationManager? = null

    @Bean
    @Throws(java.lang.Exception::class)
    open fun filterChain(http: HttpSecurity, mvc: MvcRequestMatcher.Builder): SecurityFilterChain {
        http.csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests { authorize ->
                run {
//                    val ignoreUrls = ignoreUrlsConfig!!.urls
//                    for (ignoreUrl in ignoreUrls) {
//                        authorize.requestMatchers(mvc.pattern(ignoreUrl)).permitAll()
//                    }
                    authorize.anyRequest().access(dynamicAuthorizationManager)
                }
            }
            .exceptionHandling { handlingConfigurer ->
                run {
                    handlingConfigurer.accessDeniedHandler(restfulAccessDeniedHandler)
                    handlingConfigurer.authenticationEntryPoint(restAuthenticationEntryPoint)
                }
            }.httpBasic(withDefaults())
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
//            .logout { logout: LogoutConfigurer<HttpSecurity?> ->
//                logout
//                    .logoutSuccessHandler(logoutSuccessHandler) //登录成功
//                    .invalidateHttpSession(true)
//            }
//            .formLogin { formLogin: FormLoginConfigurer<HttpSecurity?> ->
//                formLogin
//                    .successHandler(authenticationSuccessHandler) //登录成功
//                    .failureHandler(authenticationFailureHandler)
//            } //登录失败

        return http.build()
    }


}
