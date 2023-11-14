package com.ityu.sdf.api.config

import com.ityu.common.service.system.UserService
import com.ityu.common.utils.Constants
import com.ityu.common.utils.HttpUtil
import com.ityu.common.utils.JwtTokenUtil
import com.ityu.common.utils.StringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import java.util.*

/**
 * 审计功能配置
 *
 * @author enilu
 * @version 2019/1/8 0008
 */
@Configuration
open class UserIDAuditorConfig : AuditorAware<Long> {
    @Autowired
    val jwtTokenUtil: JwtTokenUtil? = null

    @Autowired
    val userService: UserService? = null
    override fun getCurrentAuditor(): Optional<Long> {
        try {
            val token = HttpUtil.getRequest().getHeader("Authorization")
            if (StringUtil.isNotEmpty(token)) {
                val userName = jwtTokenUtil?.getUserNameFromToken(token)
                val user = userService?.findByAccountForLogin(userName);
                if (userName?.isNotBlank() == true&&user!=null) {
                    return Optional.of(user.id!!)
                }
            }
        } catch (e: Exception) {
            //返回系统用户id
            return Optional.of(Constants.SYSTEM_USER_ID)
        }
        //返回系统用户id
        return Optional.of(Constants.SYSTEM_USER_ID)
    }
}
