package com.ityu.common.bean.entity.system

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import jakarta.persistence.*

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_user")
@Table(name = "t_sys_user", )
@EntityListeners(
    AuditingEntityListener::class
)
open class User : BaseEntity() {
    @Column(columnDefinition = "varchar(64) comment '头像'")
     open var avatar: String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '账户'")
     open var account: String? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT '密码'")
     open var password: String? = null

    @Column(columnDefinition = "VARCHAR(16) COMMENT '密码盐'")
     open var salt: String? = null

    @Column(columnDefinition = "VARCHAR(32) COMMENT '姓名'")
     open var name: String? = null

    @Column(columnDefinition = "DATE COMMENT '生日'")
     open var birthday: Date? = null

    @Column(columnDefinition = "INT COMMENT '性别:1:男,2:女'")
     open var sex: Int? = null

    @Column(columnDefinition = "VARCHAR(64) COMMENT 'email'")
     open var email: String? = null

    @Column(columnDefinition = "VARCHAR(16) COMMENT '手机号'")
     open var phone: String? = null

    @Column(columnDefinition = "VARCHAR(128) COMMENT '角色id列表，以逗号分隔'")
     open var roleid: String? = null

    @Column(columnDefinition = "BIGINT COMMENT '部门id'")
     open var deptid: Long? = null

    @Column(columnDefinition = "INT COMMENT '状态1:启用,2:禁用'")
     open var status: Int? = null

    @Column(columnDefinition = "INT COMMENT '版本'")
     open var version: Int? = null

    @JoinColumn(
        name = "deptid",
        insertable = false,
        updatable = false,
        foreignKey = ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    @ManyToOne(fetch = FetchType.EAGER)
      open var dept: Dept? = null
}
