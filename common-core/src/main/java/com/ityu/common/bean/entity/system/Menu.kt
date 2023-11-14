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
@Entity(name = "t_sys_menu")
@Table(name = "t_sys_menu", )
@EntityListeners(
    AuditingEntityListener::class
)
open class Menu : BaseEntity() {
    @Column(columnDefinition = " open varCHAR(32) COMMENT '编号'", unique = true, nullable = false)
    open var code: @NotBlank(message = "编号不能为空") String? = null

    @Column(columnDefinition = " open  open varCHAR(64) COMMENT '父菜单编号'", nullable = false)
    open var pcode: String? = null

    @Column(columnDefinition = " open  open varCHAR(128) COMMENT '递归父级菜单编号'")
    open var pcodes: String? = null

    @Column(columnDefinition = " open  open varCHAR(64) COMMENT '名称'", nullable = false)
    open var name: @NotBlank(message = "名称不能为空") String? = null

    @Column(columnDefinition = " open  open varCHAR(32) COMMENT '图标'")
    open var icon: String? = null

    /**
     * 如果当前配置为非菜单（按钮）也需要配置链接，v-permission使用该配置，且不能与其他url重复
     */
    @Column(columnDefinition = " open varCHAR(64) COMMENT '资源/权限标识'")
    open var url: String? = null

    @Column(columnDefinition = "INT COMMENT '顺序'", nullable = false)
    open var num: Int? = null

    @Column(columnDefinition = "INT COMMENT '级别'", nullable = false)
    open var levels: Int? = null

    @Column(columnDefinition = "INT COMMENT '是否是菜单1:菜单,0:按钮'", nullable = false)
    open var ismenu: Int? = null

    @Column(columnDefinition = " open varCHAR(32) COMMENT '鼠标悬停提示信息'")
    open var tips: String? = null

    @Column(columnDefinition = "INT COMMENT '是否默认打开1:是,0:否'")
    open var isopen: Int? = null

    @Column(columnDefinition = " open varCHAR(64) COMMENT '页面组件'")
    open var component: String? = null

    @Column(columnDefinition = "TINYINT COMMENT '是否隐藏'")
    open var hidden = false
}
