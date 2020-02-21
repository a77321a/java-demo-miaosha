package com.xsn.response;

public class CommonReturnType {
//    表明请求返回成功或者失败
    private String status;
//    若status =success 则data内 返回json
//    若 status=fail 则data内返回 错误码格式
    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
