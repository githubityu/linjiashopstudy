package com.ityu.common.bean.entity.system

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_dict")
@Table(name = "t_sys_dict", )
@EntityListeners(
    AuditingEntityListener::class
)
open class Dict : BaseEntity() {
    @Column(columnDefinition = "VARCHAR(32) COMMENT '字典值'")
    open var num: String? = null

    @Column(columnDefinition = "BIGINT COMMENT '字典记录所属组id'")
    open var pid: Long? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '字典显示值'")
    open var name: String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '备注'")
    open var tips: String? = null
}
