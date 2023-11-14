package com.ityu.security.config

import com.ityu.common.config.BaseRedisConfig
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration

/**
 * Redis相关配置
 * Created by macro on 2020/3/2.
 */
@EnableCaching
@Configuration
open class RedisConfig : BaseRedisConfig()
