package com.ityu.common.bean.entity.system

import com.ityu.common.bean.entity.BaseEntity
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners

/**
 * Created  on 2018/4/2 0002.
 * 系统通知，改表并没有真正用起来，可以根据自己实际需要做调整
 * @author enilu
 */
@Entity(name = "t_sys_notice")
@Table(name = "t_sys_notice", )
@EntityListeners(
    AuditingEntityListener::class
)
open class Notice : BaseEntity() {
    @Column(columnDefinition = "VARCHAR(64) COMMENT '通知标题'")
    open var title: String? = null

    @Column(columnDefinition = "INT COMMENT '通知类型'")
    open var type: Int? = null

    @Column(columnDefinition = "TEXT COMMENT '通知内容'")
    open var content: String? = null
}
