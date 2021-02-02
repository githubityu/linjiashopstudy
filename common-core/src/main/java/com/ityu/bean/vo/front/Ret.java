package com.ityu.bean.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="Ret",description="统一数据信息")
public class Ret<T> {
    @ApiModelProperty(value="状态吗 0成功 1失败",name="code",example="0")
    public Integer code;
    @ApiModelProperty(value="状态吗 true成功 false失败",name="status",example="0")
    public boolean status;
    @ApiModelProperty(value="错误信息",name="msg",example="密码不对")
    public String msg;
    @ApiModelProperty(value="对象信息",name="data")
    public T data;

    public Ret(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.status = Rets.SUCCESS.intValue() == code.intValue();
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("'code':").append(code).append(",");
        builder.append("'msg':").append(msg).append(",");
        builder.append("'success':").append(status).append(",");
        builder.append("}");
        return builder.toString();
    }
}
