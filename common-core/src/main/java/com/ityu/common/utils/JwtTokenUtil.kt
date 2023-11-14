package com.ityu.common.utils

import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.StrUtil
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.util.*

/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Created by macro on 2018/4/26.
 */
class JwtTokenUtil {
    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    @Value("\${jwt.tokenHead}")
    private val tokenHead: String? = null

    /**
     * 根据负责生成JWT的token
     */
    private fun generateToken(claims: Map<String, Any?>): String {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(SignatureAlgorithm.HS512, CryptUtil.encodeBASE64(secret))
            .compact()
    }

    /**
     * 从token中获取JWT中的负载
     */
    private fun getClaimsFromToken(token: String): Claims? {
        var claims: Claims? = null
        try {
            claims = Jwts.parser()
                .setSigningKey(CryptUtil.encodeBASE64(secret)).build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            LOGGER.info("JWT格式验证失败:{}", token)
        }
        return claims
    }

    /**
     * 生成token的过期时间
     */
    private fun generateExpirationDate(): Date {
        return Date(System.currentTimeMillis() + expiration!! * 1000)
    }

    /**
     * 从token中获取登录用户名
     */
    fun getUserNameFromToken(token: String): String? {
        return getUserInfoFromToken(token).first
    }

    fun getUserIdFromToken(token: String): Long {
        return getUserInfoFromToken(token).second
    }

    private fun getUserInfoFromToken(token: String): Pair<String?, Long> {
        var token = token
        token = getTokenRemoveHead(token) // The
        var username: String?
        var userId = 0L
        try {
            val claims = getClaimsFromToken(token)
            username = claims!!.subject
            userId = claims[CLAIM_KEY_USERID].toString().toLong()
        } catch (e: Exception) {
            username = null
        }
        return Pair(username, userId)
    }

    fun getTokenRemoveHead(token: String): String {
        var token = token
        if (token.startsWith(tokenHead!!)) {
            token = token.substring(tokenHead.length) // The
        }
        return token
    }

    /**
     * 验证token是否还有效
     *
     * @param token    客户端传入的token
     * @param userName 从数据库中查询出来的用户信息
     */
    fun validateToken(token: String, userName: String): Boolean {
        val username = getUserNameFromToken(token)
        return username == userName && !isTokenExpired(token)
    }

    /**
     * 判断token是否已经失效
     */
    private fun isTokenExpired(token: String): Boolean {
        val expiredDate = getExpiredDateFromToken(token)
        return expiredDate.before(Date())
    }

    /**
     * 从token中获取过期时间
     */
    private fun getExpiredDateFromToken(token: String): Date {
        val claims = getClaimsFromToken(token)
        return claims!!.expiration
    }

    /**
     * 根据用户信息生成token
     */
    fun generateToken(userName: String?, userId: Long?): String {
        val claims: MutableMap<String, Any?> = HashMap()
        claims[CLAIM_KEY_USERNAME] = userName
        claims[CLAIM_KEY_USERID] = userId
        claims[CLAIM_KEY_CREATED] = Date()
        return generateToken(claims)
    }

    /**
     * 当原来的token没过期时是可以刷新的
     *
     * @param oldToken 带tokenHead的token
     */
    fun refreshHeadToken(oldToken: String): String? {
        if (StrUtil.isEmpty(oldToken)) {
            return null
        }
        val token = oldToken.substring(tokenHead!!.length)
        if (StrUtil.isEmpty(token)) {
            return null
        }
        //token校验不通过
        val claims = getClaimsFromToken(token) ?: return null
        //如果token已经过期，不支持刷新
        if (isTokenExpired(token)) {
            return null
        }
        //如果token在30分钟之内刚刷新过，返回原token
        return if (tokenRefreshJustBefore(token, 30 * 60)) {
            token
        } else {
            claims[CLAIM_KEY_CREATED] = Date()
            generateToken(claims)
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     *
     * @param token 原token
     * @param time  指定时间（秒）
     */
    private fun tokenRefreshJustBefore(token: String, time: Int): Boolean {
        val claims = getClaimsFromToken(token)
        val created = claims!!.get(CLAIM_KEY_CREATED, Date::class.java)
        val refreshDate = Date()
        //刷新时间在创建时间的指定时间内
        return refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time))
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JwtTokenUtil::class.java)
        private const val CLAIM_KEY_USERNAME = "sub"
        private const val CLAIM_KEY_USERID = "userId"
        private const val CLAIM_KEY_CREATED = "created"
    }
}
