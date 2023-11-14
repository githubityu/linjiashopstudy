package com.ityu.common.bean.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import java.io.Serializable
import java.util.*

/**
 * Created  on 2019/1/8 0002.
 *
 * @author enilu
 */
@MappedSuperclass
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
abstract class BaseEntity : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @CreationTimestamp
    @Column(name = "create_time", columnDefinition = "DATETIME COMMENT '创建时间/注册时间'", updatable = false)
    open var createTime: Date? = null

    @Column(name = "create_by", columnDefinition = "bigint COMMENT '创建人'", updatable = false)
    @CreatedBy
    open var createBy: Long? = null


    @UpdateTimestamp
    @Column(name = "modify_time", columnDefinition = "DATETIME COMMENT '最后更新时间'")
    open var modifyTime: Date? = null

    @LastModifiedBy
    @Column(name = "modify_by", columnDefinition = "bigint COMMENT '最后更新人'")
    open var modifyBy: Long? = null
}
