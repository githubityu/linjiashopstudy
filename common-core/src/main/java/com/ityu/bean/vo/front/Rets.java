package com.ityu.bean.vo.front;



public class Rets {

    public static final Integer SUCCESS = 0;
    public static final Integer FAILURE = 1;
    public static final Integer TOKEN_EXPIRE = 50014;

    public static Ret success(Object data) {
        return new Ret(Rets.SUCCESS, "成功", data);
    }

    public static Ret error(Integer code, String msg) {
        return new Ret(code, msg, null);
    }

    public static Ret failure(String msg) {
        return new Ret(Rets.FAILURE, msg, null);
    }

    public static Ret success() {
        return new Ret(Rets.SUCCESS, "成功", null);
    }

    public static Ret expire() {
        return new Ret(Rets.TOKEN_EXPIRE, "token 过期", null);
    }
}
