package com.xsn.service;

import com.xsn.error.BusinessException;
import com.xsn.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId,Integer goodsId,Integer amount) throws BusinessException;
}
