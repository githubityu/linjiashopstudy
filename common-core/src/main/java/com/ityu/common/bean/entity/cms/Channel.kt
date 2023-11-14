package com.ityu.common.bean.entity.cms

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.validation.constraints.NotBlank

@Entity(name = "t_cms_channel")
@Table(name = "t_cms_channel")
@EntityListeners(
    AuditingEntityListener::class
)
open class Channel : BaseEntity() {
    @Column(columnDefinition = "VARCHAR(64) COMMENT '名称'")
    open var name: @NotBlank(message = "名称不能为空") String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '编码'")
    open var code: @NotBlank(message = "编码不能为空") String? = null
}
