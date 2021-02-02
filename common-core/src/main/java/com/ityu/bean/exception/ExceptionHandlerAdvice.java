package com.ityu.bean.exception;



import com.ityu.bean.vo.front.Ret;
import com.ityu.bean.vo.front.Rets;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = ToolBoxException.class)
    public Ret<String> doBaseApiException(ToolBoxException e) {
        return Rets.failure(e.getMessage());
    }

}
