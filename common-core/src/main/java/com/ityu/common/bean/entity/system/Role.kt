package com.ityu.common.bean.entity.system

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Transient
import jakarta.validation.constraints.NotBlank

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_role")
@Table(name = "t_sys_role", )
open class Role : BaseEntity() {
    @Column(columnDefinition = "INT COMMENT '排序'")
    open var num: Int? = null

    @Column(columnDefinition = "BIGINT COMMENT '父角色id'")
    open var pid: Long? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '角色名称'")
    open var name: @NotBlank(message = "角色名称不能为空") String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '角色编码'")
    open var code: String? = null

    @Column(columnDefinition = "BIGINT COMMENT '角色所属部门'")
    open var deptid: Long? = null

    @Column(columnDefinition = "INT COMMENT '角色版本号'")
    open var version: Int? = null

    @Transient
    open val label = name
}
