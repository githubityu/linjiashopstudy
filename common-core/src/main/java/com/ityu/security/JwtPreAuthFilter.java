package com.ityu.security;


import com.ityu.bean.enumeration.BizExceptionEnum;
import com.ityu.utils.HttpUtil;
import com.ityu.utils.JwtTokenUtil;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author: 倪明辉
 * @date: 2019/3/6 16:20
 * @description: 对所有请求进行过滤
 * BasicAuthenticationFilter继承于OncePerRequestFilter==》确保在一次请求只通过一次filter，而不需要重复执行。
 */
public class JwtPreAuthFilter extends BasicAuthenticationFilter {


    public JwtPreAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * description: 从request的header部分读取Token
     *
     * @param request
     * @param response
     * @param chain
     * @return void
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        request = HttpUtil.getRequest();
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader, request));
        super.doFilterInternal(request, response, chain);
    }

    /**
     * description: 读取Token信息，创建UsernamePasswordAuthenticationToken对象
     *
     * @param tokenHeader
     * @return org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader, HttpServletRequest request) {
        //解析Token时将“Bearer ”前缀去掉
        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        try {
            AdminUser user = UserService.me().adminUserForFilter(token);
            if (user == null) {
                throw new AccessDeniedException(BizExceptionEnum.TOKEN_INVALID.getMessage());
            }
            request.setAttribute("user", user);
            String username = user.getUsername();
            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
            }
        } catch (Exception e) {
            throw new AccessDeniedException(BizExceptionEnum.TOKEN_INVALID.getMessage());
        }
        return null;

    }
}