package com.xsn.service;

import com.xsn.error.BusinessException;
import com.xsn.service.model.OrderModel;

public interface OrderService {
    //1、通过前端url上传过来秒杀活动id,然后下单接口校验对应商品且活动已开始
    //2、直接在下单接口内判断对应商品是否存在秒杀活动，若存在进行中的则以秒杀价格下单
    //说明：入口不同秒杀活动可能不同
    OrderModel createOrder(Integer userId,Integer goodsId,Integer amount,Integer promoId) throws BusinessException;
    
}
