package com.ityu.security.component

import cn.hutool.core.collection.CollUtil
import com.ityu.security.config.IgnoreUrlsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.util.AntPathMatcher
import org.springframework.util.PathMatcher
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * 动态鉴权管理器，用于判断是否有资源的访问权限
 * Created by macro on 2023/11/3.
 */
open class DynamicAuthorizationManager : AuthorizationManager<RequestAuthorizationContext> {
    @Autowired
    private val securityDataSource: DynamicSecurityMetadataSource? = null

    @Autowired
    private val ignoreUrlsConfig: IgnoreUrlsConfig? = null
    override fun verify(authentication: Supplier<Authentication>, `object`: RequestAuthorizationContext) {
        super.verify(authentication, `object`)
    }

    override fun check(
        authentication: Supplier<Authentication>,
        requestAuthorizationContext: RequestAuthorizationContext
    ): AuthorizationDecision? {
        val request = requestAuthorizationContext.request
        val path = request.requestURI
        val pathMatcher: PathMatcher = AntPathMatcher()
        //白名单路径直接放行
        val ignoreUrls = ignoreUrlsConfig!!.urls
        for (ignoreUrl in ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, path)) {
                return AuthorizationDecision(true)
            }
        }
        //对应跨域的预检请求直接放行
        if (request.method == HttpMethod.OPTIONS.name()) {
            return AuthorizationDecision(true)
        }
        //权限校验逻辑
        val configAttributeList = securityDataSource!!.getConfigAttributesWithPath(path)
        val needAuthorities = configAttributeList.stream()
            .map { obj: ConfigAttribute -> obj.attribute }
            .collect(Collectors.toList())
        val currentAuth = authentication.get()
        //判定是否已经实现登录认证
        return if (currentAuth.isAuthenticated) {
            if (needAuthorities.isEmpty()) {
                return AuthorizationDecision(true)
            }
            val grantedAuthorities = currentAuth.authorities
            val hasAuth = grantedAuthorities.stream()
                .filter { item: GrantedAuthority? -> needAuthorities.contains(item!!.authority) }
                .collect(Collectors.toList())
            if (CollUtil.isNotEmpty(hasAuth)) {
                AuthorizationDecision(true)
            } else {
                AuthorizationDecision(false)
            }
        } else {
            AuthorizationDecision(false)
        }
    }
}
