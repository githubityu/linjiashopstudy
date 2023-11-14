package com.ityu.common.exception;

/**
 * API返回码接口
 * Created by macro on 2019/4/19.
 */
public interface IErrorCode {
    /**
     * 返回码
     */
    int getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
