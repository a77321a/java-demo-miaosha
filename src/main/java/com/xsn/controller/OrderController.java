package com.xsn.controller;

import com.mysql.cj.Session;
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
import javax.servlet.http.HttpSession;
import java.util.Map;
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController  {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @ResponseBody
    @RequestMapping(value = "/createOrder",method = {RequestMethod.POST})
    public CommonReturnType createOrder(@RequestBody Map<String,String> req, HttpSession session
                                        ) throws BusinessException {
        Integer goodsId = Integer.valueOf(req.get("goodsId")) ;
        Integer amount = Integer.valueOf(req.get("amount"));
        Boolean isLogin =   (Boolean)session.getAttribute("IS_LOGIN");
//        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIT,"请登录后下单");
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel =  orderService.createOrder(userModel.getId(),goodsId,amount);
        return CommonReturnType.create(orderModel);
    }
}
