package com.xsn.controller;

import com.mysql.cj.Session;
import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.response.CommonReturnType;
import com.xsn.service.OrderService;
import com.xsn.service.model.OrderModel;
import com.xsn.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private RedisTemplate redisTemplate;
    @ResponseBody
    @RequestMapping(value = "/createOrder",method = {RequestMethod.POST})
    public CommonReturnType createOrder(@RequestBody Map<String,String> req,HttpServletRequest hsp
                                        ) throws BusinessException {
        Integer goodsId = Integer.valueOf(req.get("goodsId")) ;
        Integer amount = Integer.valueOf(req.get("amount"));
        String token = hsp.getHeader("Authorization").toString().equals("undefined")
                ?req.get("token").toString()
                : hsp.getHeader("Authorization").toString() ;
        Integer promoId = req.get("promoId") != null ?  Integer.valueOf(req.get("promoId")) :null;
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIT,"请登录后下单");
        }
        OrderModel orderModel =  orderService.createOrder(userModel.getId(),goodsId,amount,promoId);
        return CommonReturnType.create(orderModel);
    }
}
