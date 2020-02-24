package com.xsn.error;

public enum EmBusinessError implements CommonError{
//    通用错误类型1000
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOW_ERROR(10002,"未知错误"),
//    2000开头为用户信息相关错误
    USER_NOT_EXIT(20001,"用户不存在"),
    STOCK_NOT_ENOUGH(30001,"库存不足"),
    USER_LOGIN_ERROR(20002,"登录失败，检查账号密码");
    ;
    private  EmBusinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;
    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }


}
