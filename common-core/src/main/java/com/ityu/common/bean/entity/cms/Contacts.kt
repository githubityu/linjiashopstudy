package com.ityu.common.bean.entity.cms

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.validation.constraints.NotBlank

@Entity(name = "t_cms_contacts")
@Table(name = "t_cms_contacts")
open class Contacts : BaseEntity() {
    @Column(columnDefinition = "VARCHAR(64) COMMENT '邀约人名称'")
    open var userName: @NotBlank(message = "名称不能为空") String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '联系电话'")
    open var mobile: @NotBlank(message = "手机号不能为空") String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '电子邮箱'")
    open var email: @NotBlank(message = "电子邮箱不能为空") String? = null

    @Column(columnDefinition = "VARCHAR(128) COMMENT '备注'")
    open var remark: String? = null
}
