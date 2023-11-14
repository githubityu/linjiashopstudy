package com.ityu.common.bean.vo.front

import com.ityu.common.exception.IErrorCode


object Rets {
    const val SUCCESS = 20000
    const val FAILURE = 9999
    const val TOKEN_EXPIRE = 50014
    fun success(data: Any?): Ret<*> {
        return Ret(SUCCESS, "成功", data)
    }

    fun failure(msg: String = "", error: IErrorCode? = null): Ret<*> {
        return when (error) {
            null -> Ret<Any?>(FAILURE, msg, null)
            else -> Ret<Any?>(error.code, error.message, null)
        }
    }

    fun success(): Ret<*> {
        return Ret<Any?>(SUCCESS, "成功", null)
    }

    fun expire(): Ret<*> {
        return Ret<Any?>(TOKEN_EXPIRE, "token 过期", null)
    }
}
