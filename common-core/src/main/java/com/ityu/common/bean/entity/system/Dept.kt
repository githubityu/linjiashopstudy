package com.ityu.common.bean.entity.system

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.validation.constraints.NotBlank

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_dept")
@Table(name = "t_sys_dept")
@EntityListeners(
    AuditingEntityListener::class
)
open class Dept : BaseEntity() {
    @Column(columnDefinition = "INT COMMENT '排序'")
    open var num: Int? = null

    @Column(columnDefinition = "BIGINT COMMENT '父组织id'")
    open var pid: Long? = null

    /**
     * 从顶级组织id开始一直到父组织id，例如：[0],[1], 期中0为标识根组织(虚拟组织，实际并没有这个组织），1为父组织
     */
    @Column(columnDefinition = "VARCHAR(64) COMMENT '所有上级组织id列表'")
    open var pids: String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '部门/组织简称'")
    open var simplename: @NotBlank(message = "部门简称不能为空") String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '部门/组织全称'")
    open var fullname: @NotBlank(message = "部门全称不能为空") String? = null
}
