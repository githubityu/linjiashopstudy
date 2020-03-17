package com.ityu.linjiamobileapi.config;



import com.ityu.security.AdminUser;
import com.ityu.security.AdminUserFactroy;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 审计功能配置
 *@CreatedBy
 * @LastModifiedBy
 */
@Configuration
public class UsernameAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        AdminUser tokenFromRequest = AdminUserFactroy.me().getTokenFromRequest();
        if(tokenFromRequest!=null){
            return Optional.of(tokenFromRequest.getId()+"");
        }else{
            return Optional.of("0");
        }
    }
}
