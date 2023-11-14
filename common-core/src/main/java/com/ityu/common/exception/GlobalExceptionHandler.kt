package com.ityu.common.exception

import cn.hutool.core.util.StrUtil
import com.ityu.common.bean.vo.front.Ret
import com.ityu.common.bean.vo.front.Rets.failure
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLSyntaxErrorException

/**
 * 全局异常处理类
 * Created by macro on 2020/2/27.
 */
@ControllerAdvice
class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = [ApiException::class])
    fun handle(e: ApiException): Ret<*> {
        return if (e.errorCode != null) {
            failure(error = e.errorCode)
        } else failure(msg = e.message!!)
    }



    @ResponseBody
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidException(e: MethodArgumentNotValidException): Ret<*> {
        val bindingResult = e.bindingResult
        var message: String? = null
        if (bindingResult.hasErrors()) {
            val fieldError = bindingResult.fieldError
            if (fieldError != null) {
                message = fieldError.field + fieldError.defaultMessage
            }
        }
        return failure(msg = message!!)
    }

    @ResponseBody
    @ExceptionHandler(value = [BindException::class])
    fun handleValidException(e: BindException): Ret<*> {
        val bindingResult = e.bindingResult
        var message: String? = null
        if (bindingResult.hasErrors()) {
            val fieldError = bindingResult.fieldError
            if (fieldError != null) {
                message = fieldError.field + fieldError.defaultMessage
            }
        }
        return failure(msg = message!!)
    }

    @ResponseBody
    @ExceptionHandler(value = [SQLSyntaxErrorException::class])
    fun handleSQLSyntaxErrorException(e: SQLSyntaxErrorException): Ret<*> {
        var message = e.message
        if (StrUtil.isNotEmpty(message) && message!!.contains("denied")) {
            message = "演示环境暂无修改权限，如需修改数据可本地搭建后台服务！"
        }
        return failure(msg = message!!)
    }
//    @ResponseBody
//    @ExceptionHandler(value = [SerializationException::class])
//    fun handleSerializationException(e: SerializationException): Ret<*> {
//        var message = e.message
//        return failure(msg = message!!)
//    }

}
