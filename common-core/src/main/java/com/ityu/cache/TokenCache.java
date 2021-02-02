package com.ityu.cache;


import com.ityu.cache.impl.EhcacheDao;
import com.ityu.security.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户登录时，生成的Token与用户ID的对应关系
 */
@Service
public class TokenCache {

    @Autowired
    private CacheDao ehcacheDao;


    public void putToken(String token, long idUser) {
        ehcacheDao.set(idUser, token);
    }

    public String getToken(long idUser) {
        return ehcacheDao.get(idUser);
    }

    public void removeToken(long idUser) {
        ehcacheDao.del(idUser);
    }
//    public Long get(String token) {
//        return ehcacheDao.hget(EhcacheDao.SESSION, token, Long.class);
//    }
//
//    public Long getIdUser() {
//        return ehcacheDao.hget(EhcacheDao.SESSION, HttpUtil.getToken(), Long.class);
//    }
//
//    public void remove(String token) {
//        ehcacheDao.hdel(EhcacheDao.SESSION, token + "user");
//    }

    public void setUser(String token, AdminUser shiroUser) {
        ehcacheDao.hset(EhcacheDao.SESSION, token + "user", shiroUser);
    }

    public AdminUser getUser(String token) {
        return ehcacheDao.hget(EhcacheDao.SESSION, token + "user", AdminUser.class);
    }


}
