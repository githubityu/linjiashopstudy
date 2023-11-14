package com.ityu.sdf.api

import com.ityu.common.dao.BaseRepositoryFactoryBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@SpringBootApplication
@EntityScan(basePackages = ["com.ityu.common.bean.entity"])
@ComponentScan(basePackages = ["com.ityu"])
@EnableJpaRepositories(
    basePackages = ["com.ityu.common.dao"],
    repositoryFactoryBeanClass = BaseRepositoryFactoryBean::class
)
@EnableJpaAuditing
open class SdfApiApplication

fun main(args: Array<String>) {
    runApplication<SdfApiApplication>(*args)
}
