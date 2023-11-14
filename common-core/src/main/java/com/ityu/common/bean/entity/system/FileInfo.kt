package com.ityu.common.bean.entity.system

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Transient

@Entity(name = "t_sys_file_info")
@Table(name = "t_sys_file_info")
@EntityListeners(
    AuditingEntityListener::class
)
open class FileInfo : BaseEntity() {
    @Column(columnDefinition = "VARCHAR(64) COMMENT '原始文件名称'")
    open var originalFileName: String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '文件存储在磁盘中的真正名称'")
    open var realFileName: String? = null

    @Transient
    open var ablatePath: String? = null
}
