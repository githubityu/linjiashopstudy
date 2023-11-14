package com.ityu.common.bean.entity.system

import jakarta.persistence.Table
import java.util.*
import jakarta.persistence.*

/**
 *
 *
 * User: Yao
 * Date: 2017-06-22 11:12:48
 */
@Table(name = "t_sys_task_log")
@Entity(name = "t_sys_task_log")
open class TaskLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(columnDefinition = "BIGINT COMMENT '对应任务id'")
    open var idTask: Long? = null

    @Column(columnDefinition = "VARCHAR(50) COMMENT '任务名'")
    open var name: String? = null

    @Column(columnDefinition = "DATETime COMMENT '执行时间'")
    open var execAt: Date? = null

    @Column(columnDefinition = "INTEGER COMMENT '执行结果（成功:1、失败:0)'")
    open var execSuccess = 0

    @Column(columnDefinition = "VARCHAR(500) COMMENT '抛出异常'")
    open var jobException: String? = null

    companion object {
        const val EXE_FAILURE_RESULT = 0
        const val EXE_SUCCESS_RESULT = 1
    }
}