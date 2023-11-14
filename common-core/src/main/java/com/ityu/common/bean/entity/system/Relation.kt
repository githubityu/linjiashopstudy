package com.ityu.common.bean.entity.system

import jakarta.persistence.Table
import jakarta.persistence.*

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_relation")
@Table(name = "t_sys_relation", )
open class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     open var id: Long? = null

    @Column
     open var menuid: Long? = null

    @Column
     open var roleid: Long? = null
}
