package com.ityu.common.bean.core

import java.io.Serializable

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 *
 * @author fengshuonan
 * @date 2016年12月5日 上午10:26:43
 */
open class AdminUser : Serializable {
    var id: Long? = null // 主键ID
    var account: String? = null // 账号
    var password: String? = null
    var name: String? = null // 姓名
    var deptId: Long? = null // 部门id
    var roleList: List<Long>? = null // 角色集
    var deptName: String? = null // 部门名称
    var roleNames: List<String>? = null // 角色名称集
    var roleCodes: List<String>? = null //角色编码
    var urls: Set<String>? = null //资源路径
    var permissions: Set<String>? = null //资源编码

    companion object {
        private const val serialVersionUID = 1L
    }
}
