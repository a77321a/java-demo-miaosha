package com.xsn.controller;

import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    //    定义exceptionhandler 解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception exp){
        Map<String,Object> resp = new HashMap<>();
        if(exp instanceof BusinessException){
            BusinessException businessException = (BusinessException)exp;
            resp.put("errCode",businessException.getErrCode());
            resp.put("errMsg",businessException.getErrMsg());
        }else{
            resp.put("errCode", EmBusinessError.UNKNOW_ERROR.getErrCode());
            resp.put("errMsg",EmBusinessError.UNKNOW_ERROR.getErrMsg());
        }
        return CommonReturnType.create(resp,"fail");
    }
}
