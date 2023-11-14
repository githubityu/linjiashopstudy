package com.ityu.sdf.api.config

import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.support.jakarta.StatViewServlet
import com.alibaba.druid.support.jakarta.WebStatFilter
import jakarta.servlet.Filter
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.sql.DataSource
/**
 * descript
 *
 * @Author enilu
 * @Date 2021/6/6 1:56
 * @Version 1.0
 * http://localhost:8087/druid/index.html
 */
@Configuration
open class DruidConfiguration(private val env: Environment) {
    private val logger = LoggerFactory.getLogger(DruidConfiguration::class.java)

    @Bean
    open fun druidServlet(): ServletRegistrationBean<*> {
        val servletRegistrationBean = ServletRegistrationBean(StatViewServlet(), "/druid/*")
        // IP白名单
//        servletRegistrationBean.addInitParameter("allow", "*");
        // IP黑名单(共同存在时，deny优先于allow)
//        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        //控制台管理用户
        val user = env.getProperty("spring.datasource.druid.stat-view-servlet.login-username")
        val password = env.getProperty("spring.datasource.druid.stat-view-servlet.login-password")
        servletRegistrationBean.addInitParameter("loginUsername", user)
        servletRegistrationBean.addInitParameter("loginPassword", password)
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false")
        return servletRegistrationBean
    }

    @Bean
    open fun filterRegistrationBean(): FilterRegistrationBean<*> {
        val filterRegistrationBean: FilterRegistrationBean<*> = FilterRegistrationBean<Filter>(WebStatFilter())
        filterRegistrationBean.addUrlPatterns("/*")
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*")
        return filterRegistrationBean
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    open fun druid(): DataSource {
        return DruidDataSource()
    }

}
