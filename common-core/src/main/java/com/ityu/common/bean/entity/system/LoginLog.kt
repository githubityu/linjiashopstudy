package com.ityu.common.bean.entity.system

import org.hibernate.annotations.CreationTimestamp
import jakarta.persistence.Table
import java.util.*
import jakarta.persistence.*

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_login_log")
@Table(name = "t_sys_login_log", )
open class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '日志表述'")
    open var logname: String? = null

    @Column(columnDefinition = "INT COMMENT '操作用户id'")
    open var userid: Int? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '操作结果标识'")
    open var succeed: String? = null

    @Column(columnDefinition = "TEXT COMMENT '日志详情'")
    open var message: String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '操作ip'")
    open var ip: String? = null

    @CreationTimestamp
    @Column(name = "create_time", columnDefinition = "DATETIME COMMENT '创建时间'")
    open var createTime: Date? = null
}
