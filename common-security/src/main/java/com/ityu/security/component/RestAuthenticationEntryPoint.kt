package com.ityu.security.component

import cn.hutool.json.JSONUtil
import com.ityu.common.bean.vo.front.Rets.failure
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException

/**
 * 自定义返回结果：未登录或登录过期
 * Created by macro on 2018/5/14.
 */
open class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Cache-Control", "no-cache")
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.println(JSONUtil.parse(failure(authException.message!!, null)))
        response.writer.flush()
    }
}
