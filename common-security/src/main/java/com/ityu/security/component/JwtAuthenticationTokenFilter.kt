package com.ityu.security.component

import com.ityu.common.utils.JwtTokenUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

/**
 * JWT登录授权过滤器
 * Created by macro on 2018/4/26.
 */
open class JwtAuthenticationTokenFilter : OncePerRequestFilter() {
    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Autowired
    private val jwtTokenUtil: JwtTokenUtil? = null

    @Value("\${jwt.tokenHeader}")
    private val tokenHeader: String? = null

    @Value("\${jwt.tokenHead}")
    private val tokenHead: String? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authHeader = request.getHeader(tokenHeader)
        if (authHeader != null && authHeader.startsWith(tokenHead!!)) {
            val authToken = authHeader.substring(tokenHead.length) // The part after "Bearer "
            val username = jwtTokenUtil!!.getUserNameFromToken(authToken)
            LOGGER.info("checking username:{}", username)
            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService!!.loadUserByUsername(username)
                if (jwtTokenUtil.validateToken(authToken, userDetails.username)) {
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    LOGGER.info("authenticated user:{}", username)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        chain.doFilter(request, response)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter::class.java)
    }
}
