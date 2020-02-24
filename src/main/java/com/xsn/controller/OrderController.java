package com.xsn.controller;

import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.response.CommonReturnType;
import com.xsn.service.OrderService;
import com.xsn.service.model.OrderModel;
import com.xsn.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController  {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @ResponseBody
    @RequestMapping(value = "/createOrder",method = {RequestMethod.POST},consumes = {"application/x-www-form-urlencoded"})
    public CommonReturnType createOrder(@RequestParam(name = "goodsId") Integer goodsId,
                                        @RequestParam(name = "amount") Integer amount
                                        ) throws BusinessException {
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIT,"请登录后下单");
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel =  orderService.createOrder(userModel.getId(),goodsId,amount);
        return CommonReturnType.create(orderModel);
    }
}
