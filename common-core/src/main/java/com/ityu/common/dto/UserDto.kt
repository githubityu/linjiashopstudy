package com.ityu.common.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

/**
 * 用户传输bean
 *
 * @author stylefeng
 * @Date 2017/5/5 22:40
 */
data class UserDto(
    open var id: Long? = null,
    @field:NotEmpty(message = "账号不能为空")
    @field:NotNull(message = "名称不能为空！")
    open var account: String? = null,

    @field:NotEmpty(message = "密码不能为空！")
    @field:NotNull(message = "密码不能为空")
    open var password: String? = null,
    open var salt: String? = null,

    @field:NotEmpty(message = "姓名不能为空")
    @field:NotNull(message = "姓名不能为空！")
    open var name: String = "",

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    open var birthday: Date? = null,
    open var sex: Int? = null,
    open var email: String? = null,
    open var phone: String? = null,
    open var roleid: String? = null,


    @field:Min(0, message = "所属部门不能为空！")
    open var deptid: Long? = null,
    open var status: Int? = null,
    open var createtime: Date? = null,
    open var version: Int? = null,
    open var avatar: String? = null
)