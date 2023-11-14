package com.ityu.common.bean.entity.system

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_cfg")
@Table(name = "t_sys_cfg")
@EntityListeners(
    AuditingEntityListener::class
)
open class Cfg : BaseEntity() {
    @Column(name = "cfg_name", columnDefinition = "VARCHAR(32) COMMENT '参数名'")
    open var cfgName: @NotBlank(message = "参数名不能") String? = null

    @Column(name = "cfg_value", columnDefinition = "VARCHAR(512) COMMENT '参数值'")
    open var cfgValue: @NotBlank(message = "参数值不能为空") String? = null

    @Column(name = "cfg_desc", columnDefinition = "VARCHAR(128) COMMENT '备注'")
    open var cfgDesc: String? = null
}
