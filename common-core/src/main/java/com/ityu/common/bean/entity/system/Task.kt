package com.ityu.common.bean.entity.system

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.validation.constraints.NotBlank

@Entity(name = "t_sys_task")
@Table(name = "t_sys_task", )
@EntityListeners(
    AuditingEntityListener::class
)
open class Task : BaseEntity() {
    @Column(columnDefinition = "VARCHAR(50) COMMENT '任务名'")
    open var name: @NotBlank(message = "名称不能为空") String? = null

    @Column(name = "job_group", columnDefinition = "VARCHAR(50) COMMENT '任务组名'")
    open var jobGroup: String? = null

    @Column(name = "job_class", columnDefinition = "VARCHAR(255) COMMENT '执行类'")
    open var jobClass: @NotBlank(message = "执行类不能为空") String? = null

    @Column(name = "note", columnDefinition = "VARCHAR(255) COMMENT '任务说明'")
    open var note: String? = null

    @Column(name = "cron", columnDefinition = "VARCHAR(50) COMMENT '定时规则'")
    open var cron: @NotBlank(message = "定时规则不能为空") String? = null

    @Column(name = "concurrent", columnDefinition = "TINYINT COMMENT '是否允许并发'")
    open var concurrent = false

    @Column(name = "data", columnDefinition = "TEXT COMMENT '执行参数'")
    open var data: String? = null

    @Column(name = "exec_at", columnDefinition = "DateTime COMMENT '执行时间'")
    open var execAt: Date? = null

    @Column(name = "exec_result", columnDefinition = "text COMMENT '执行结果'")
    open var execResult: String? = null

    @Column(name = "disabled", columnDefinition = "TINYINT COMMENT '是否禁用'")
    open var disabled = false
}
