package com.ityu.common.bean.vo.front

import com.fasterxml.jackson.annotation.JsonProperty

open class Ret<T> {
    @JsonProperty("code")
    open var code: Int? = null

    @JsonProperty("msg")
    open var msg: String? = null

    @JsonProperty("data")
    open var data: T? = null

    @JsonProperty("success")
    open var success = false

    constructor(code: Int, msg: String?, data: T) {
        this.code = code
        this.msg = msg
        this.data = data
        success = Rets.SUCCESS == code
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("{")
        builder.append("'code':").append(code).append(",")
        builder.append("'msg':").append(msg).append(",")
        builder.append("'success':").append(success).append(",")
        builder.append("}")
        return builder.toString()
    }
}
