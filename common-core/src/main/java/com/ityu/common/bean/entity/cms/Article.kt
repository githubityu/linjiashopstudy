package com.ityu.common.bean.entity.cms

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.jpa.domain.support.AuditingEntityListener


//"文章"
@Entity(name = "t_cms_article")
@Table(name = "t_cms_article")
@EntityListeners(
    AuditingEntityListener::class
)
open class Article : BaseEntity() {
    @Column(columnDefinition = "BIGINT COMMENT '栏目id'")
    open var idChannel: @NotNull(message = "栏目不能为空") Long? = null

    @Column(columnDefinition = "VARCHAR(128) COMMENT '标题'")
    open var title: @NotBlank(message = "标题不能为空") String? = null

    @Column(columnDefinition = "TEXT COMMENT '内容'")
    open var content: @NotBlank(message = "内容不能为空") String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '作者'")
    open var author: String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '文章题图ID'")
    open var img: String? = null
}
