package com.ityu.service.system;


import com.ityu.cache.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AccountService
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@Service
public class AccountService {
    @Autowired
    private TokenCache tokenCache;

    public void logout(long userId) {
        tokenCache.removeToken(userId);
    }

}
