package com.xsn.response;

public class CommonReturnType {
//    表明请求返回成功或者失败
    private Integer code;
//    若status =success 则data内 返回json
//    若 status=fail 则data内返回 错误码格式
    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,200);
    }
    public static CommonReturnType create(Object result,Integer code){
        CommonReturnType type = new CommonReturnType();
        type.setCode(code);
        type.setData(result);
        return type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
