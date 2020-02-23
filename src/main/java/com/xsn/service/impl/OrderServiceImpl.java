package com.xsn.service.impl;

import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.service.GoodsService;
import com.xsn.service.OrderService;
import com.xsn.service.UserService;
import com.xsn.service.model.GoodsModel;
import com.xsn.service.model.OrderModel;
import com.xsn.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer goodsId, Integer amount) throws BusinessException {
        //校验下单状态 商品是否存在，用户是否登录 购买数量是否正确
        GoodsModel goodsModel = goodsService.getGoodsDetail(goodsId);
        if(goodsModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if(userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }
        if(amount <= 0 || amount>99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }
        //落单减库存或者支付减库存

        //订单入库

    }
}
