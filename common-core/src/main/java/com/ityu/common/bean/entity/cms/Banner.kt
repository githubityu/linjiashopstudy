package com.ityu.common.bean.entity.cms

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Entity

import jakarta.persistence.EntityListeners
import jakarta.validation.constraints.NotBlank


@Entity(name = "t_cms_banner")
@Table(name = "t_cms_banner")
@EntityListeners(
    AuditingEntityListener::class
)
open class Banner : BaseEntity() {
    @Column(columnDefinition = "VARCHAR(128) COMMENT '点击banner跳转到url'")
    open var url: String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '标题'")
    open var title: @NotBlank(message = "标题不能为空") String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '类型'")
    open var type: @NotBlank(message = "类型不能为空") String? = null

    @Column(columnDefinition = "BIGINT COMMENT 'banner图id'")
    open var idFile: Long? = null
}
