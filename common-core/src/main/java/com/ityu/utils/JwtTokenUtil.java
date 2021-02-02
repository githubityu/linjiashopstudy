package com.ityu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JwtToken生成的工具类
 * JWT token的格式：header（头）、payload（负载）和signature（签名）
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * <p>
 * 如何让token失效
 * <p>
 * 就是把token存到redis中，如果用户的token修改了，就修改token
 * 多一次判断，如果token有效，再判断redis中是否匹配，匹配则有效，否则则无效
 */

@Slf4j
public class JwtTokenUtil {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static JwtTokenUtil instance = null;

    //签发者
    private static final String ISS = "Gent.Ni";
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_USER_ID = "user_id";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_UUID = "uuid";

    private static final String ROLE_CLAIMS = "role";

    private String secret = "111111";

    private Long expiration = 604800L;


    private JwtTokenUtil() {

    }

    public static JwtTokenUtil getInstance() {
        synchronized (JwtTokenUtil.class) {
            if (instance == null) {
                instance = new JwtTokenUtil();
            }
        }
        return instance;
    }


    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private String generateToken(Map<String, Object> claims, Date expirationDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public long getUserIdFromToken(String token) {
        long userId;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.get(CLAIM_KEY_USER_ID, Long.class);
        } catch (Exception e) {
            userId = -1l;
        }
        return userId;
    }

    public Pair<String, Long> getUserFromToken(String token) {
        long userId;
        String username = "";
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.get(CLAIM_KEY_USER_ID, Long.class);
            username = claims.getSubject();
        } catch (Exception e) {
            userId = -1l;
            username = "";
        }
        return new Pair(username, userId);
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> stringStream = userDetails.getAuthorities().stream().map(t -> t.getAuthority()).collect(Collectors.toSet());
        claims.put(ROLE_CLAIMS, stringStream);
        setClaims(claims, userId, userDetails.getUsername());
        return generateToken(claims);
    }

    public String generateToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        setClaims(claims, userId, username);
        return generateToken(claims);
    }

    public String generateToken(String username, Long userId, long expireTime) {
        Map<String, Object> claims = new HashMap<>();
        setClaims(claims, userId, username);
        return generateToken(claims, new Date(System.currentTimeMillis() + expireTime));
    }

    private void setClaims(Map<String, Object> claims, Long userId, String username) {
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_USER_ID, userId);
        claims.put(CLAIM_KEY_UUID, UUID.randomUUID().toString());
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    // 获取用户角色
    public Set<String> getUserRole(String token) {
        return (Set<String>) getClaimsFromToken(token).get(ROLE_CLAIMS);
    }

    public static void main(String[] args) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        //String s = jwtTokenUtil.generateToken("admin");
        String userNameFromToken = jwtTokenUtil.getUserNameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE1NzU5NjgzODUwMjksImV4cCI6MTU3NjU3MzE4NX0.aAsObZruK3fhAB7GOzO-AgQGcR7CvCXdrUoo8LJVcJ2BLWRYQ4HuO6tP7cW4FXGJ8PZEvI82RxFrL5Ano8ALig");
        System.out.println(userNameFromToken);

    }
}
