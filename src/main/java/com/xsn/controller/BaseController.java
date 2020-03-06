package com.xsn.controller;

import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.response.CommonReturnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class BaseController {
    //    定义exceptionhandler 解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, HttpServletResponse response, Exception exp){
        exp.printStackTrace();
        Map<String,Object> resp = new HashMap<>();
        if(exp instanceof BusinessException){
            BusinessException businessException = (BusinessException)exp;
            resp.put("errCode",businessException.getErrCode());
            resp.put("errMsg",businessException.getErrMsg());
        }else if(exp instanceof ServletRequestBindingException){
            resp.put("errCode",EmBusinessError.UNKNOW_ERROR);
            resp.put("errMsg","请求接口参数传递错误");
        }
        else if(exp instanceof NoHandlerFoundException){
            resp.put("errCode", EmBusinessError.UNKNOW_ERROR.getErrCode());
            resp.put("errMsg","该功能暂未开发");
        }else{
            resp.put("errCode", EmBusinessError.UNKNOW_ERROR.getErrCode());
            resp.put("errMsg",EmBusinessError.UNKNOW_ERROR.getErrMsg());
        }
        return CommonReturnType.create(resp,201);
    }
}
