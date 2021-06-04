package com.ityu.linjiamobileapi;


import com.ityu.dao.BaseRepositoryFactoryBean;
import com.ityu.linjiamobileapi.config.WxProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


/**
 * ApiApplication
 *
 * @author enilu
 * @version 2018/9/11 0011
 */
@EnableCaching
@SpringBootApplication(scanBasePackages="com.ityu")
@EntityScan(basePackages="com.ityu.bean.entity")
@EnableJpaRepositories(basePackages = "com.ityu.dao", repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableJpaAuditing
@EnableConfigurationProperties(WxProperties.class)
public class MobileApiApplication extends SpringBootServletInitializer {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MobileApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MobileApiApplication.class);
    }
}
