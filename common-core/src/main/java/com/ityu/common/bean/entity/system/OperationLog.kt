package com.ityu.common.bean.entity.system

import jakarta.persistence.Table
import java.util.*
import jakarta.persistence.*

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_operation_log")
@Table(name = "t_sys_operation_log")
open class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    /**
     * 日志类型，
     * @see com.ityu.common.bean.constant.state.LogType
     */
    @Column(columnDefinition = "VARCHAR(32) COMMENT '日志类型'")
    open var logtype: String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '日志名称'")
    open var logname: String? = null

    @Column(columnDefinition = "INT COMMENT '操作用户id'")
    open var userid: Int? = null

    @Column(columnDefinition = "VARCHAR(128) COMMENT '对应类名'")
    open var classname: String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '对应方法名'")
    open var method: String? = null

    @Column(columnDefinition = "DATE COMMENT '操作日期'")
    open var createTime: Date? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '成功标识'")
    open var succeed: String? = null

    @Column(columnDefinition = "TEXT COMMENT '详细信息'")
    open var message: String? = null
}
