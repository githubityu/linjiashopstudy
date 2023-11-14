package com.ityu.sdf.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar

/**
 * 定时任务相关配置
 * Created by macro on 2019/4/8.
 */
@Configuration
@EnableScheduling
open class SpringTaskConfig : SchedulingConfigurer {
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler())
    }

    @Bean(destroyMethod = "shutdown", name = ["taskScheduler"])
    open fun taskScheduler(): ThreadPoolTaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.setPoolSize(10) //设置任务池的大小
        scheduler.setThreadNamePrefix("task-") //设置线程的前缀名
        scheduler.setAwaitTerminationSeconds(60) //设置超时时间
        scheduler.setWaitForTasksToCompleteOnShutdown(true)
        return scheduler
    }
}
