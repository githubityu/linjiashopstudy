package com.ityu.security.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 用于配置白名单资源路径
 * Created by macro on 2018/11/5.
 */
@ConfigurationProperties(prefix = "secure.ignored")
class IgnoreUrlsConfig {
    val urls: List<String> = mutableListOf()
}
