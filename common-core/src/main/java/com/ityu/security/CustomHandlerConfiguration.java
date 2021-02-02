package com.ityu.security;


import com.ityu.bean.vo.front.Rets;
import com.ityu.cache.TokenCache;
import com.ityu.core.log.LogManager;
import com.ityu.core.log.LogTaskFactory;
import com.ityu.utils.HttpUtil;
import com.ityu.utils.JsonUtil;
import com.ityu.utils.JwtTokenUtil;

import com.ityu.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomHandlerConfiguration {

    @Autowired
    private CustomPermissionEvaluator permissionEvaluator;

    @Autowired
    private TokenCache tokenCache;

    /**
     * 访问接入点处理
     *
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        AuthenticationEntryPoint entryPoint = (request, response, e) -> {
            //NO_LOGIN
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JsonUtil.toJson(Rets.error(-1, e.getMessage())));
        };
        return entryPoint;
    }

    /**
     * 接入过后问题处理
     *
     * @return
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandler accessDeniedHandler = (request, response, e) -> {
            //NO_PERMISSION
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JsonUtil.toJson(Rets.error(-1, e.getMessage())));
        };
        return accessDeniedHandler;
    }

    /**
     * 登录成功后的处理
     * 比较的是UsernamePasswordAuthenticationFilter  UserDetails
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        AuthenticationSuccessHandler authenticationSuccessHandler = (request, response, authentication) -> {
            //返回数据
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            AdminUser currentUser = (AdminUser) SecurityUtils.getCurrentUser();
            String token = JwtTokenUtil.getInstance().generateToken(currentUser,currentUser.getId());
            String s = JwtTokenUtil.TOKEN_PREFIX + token;
            //Map数据返回
            Map<String, String> res = new HashMap<>();
            res.put("token", s);
            tokenCache.putToken(token, currentUser.getId());
            LogManager.me().executeLog(LogTaskFactory.loginLog(currentUser.getId(), HttpUtil.getIp()));
            response.getWriter().write(JsonUtil.toJson(Rets.success(res)));
        };
        return authenticationSuccessHandler;
    }

    /**
     * 登录失败后的处理
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        AuthenticationFailureHandler authenticationFailureHandler = (request, response, e) -> {
            //ACCOUNT_ERROR
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JsonUtil.toJson(Rets.error(-1, e.getMessage())));
        };
        return authenticationFailureHandler;
    }

    /**
     * 登出成功后的处理
     *
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        LogoutSuccessHandler logoutSuccessHandler = (request, response, authentication) -> {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JsonUtil.toJson(Rets.success("退出成功")));
        };
        return logoutSuccessHandler;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }
}
