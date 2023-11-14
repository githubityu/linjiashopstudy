package com.ityu.common.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size


data class TestValid(
//    @get:NotEmpty(message = "账号不能为空")
//    @get:NotBlank(message = "账号不能为空")
    @field:NotEmpty(message = "账号不能为空")
    @field:NotBlank(message = "账号不能为空")
    var account: String? = null,

//    @field:NotEmpty(message = "数量不能为空")
    @field:Min(2, message = "min 2")
    @field:Max(20, message = "max 20")
    var num: Int? = null
)