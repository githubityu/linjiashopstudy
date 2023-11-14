package com.ityu.security.component

import cn.hutool.json.JSONUtil
import com.ityu.common.bean.vo.front.Rets.failure
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.io.IOException

/**
 * 自定义返回结果：没有权限访问时
 * Created by macro on 2018/4/26.
 */
open class RestfulAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AccessDeniedException
    ) {
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Cache-Control", "no-cache")
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.println(JSONUtil.parse(failure(e.message!!, null)))
        response.writer.flush()
    }
}
