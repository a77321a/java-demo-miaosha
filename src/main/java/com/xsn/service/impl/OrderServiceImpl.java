package com.xsn.service.impl;

import com.xsn.dao.OrderInfoDOMapper;
import com.xsn.dao.SequenceDOMapper;
import com.xsn.dataobject.OrderInfoDO;
import com.xsn.dataobject.SequenceDO;
import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.service.GoodsService;
import com.xsn.service.OrderService;
import com.xsn.service.UserService;
import com.xsn.service.model.GoodsModel;
import com.xsn.service.model.OrderModel;
import com.xsn.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderInfoDOMapper orderInfoDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer goodsId, Integer amount,Integer promoId) throws BusinessException {
        //校验下单状态 商品是否存在， 
        //GoodsModel goodsModel = goodsService.getGoodsDetail(goodsId);
        GoodsModel goodsModel = goodsService.getGoodsByIdInCache(goodsId);
        if(goodsModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品不存在");
        }
        //用户是否登录
        // UserModel userModel = userService.getUserById(userId);
        UserModel userModel = userService.getUserByIdInCache(userId);
        if(userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }
        //购买数量是否正确
        if(amount <= 0 || amount>99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }
        //校验活动信息
        if(promoId!=null){
            //1、校验对应活动是否存在这个商品
            if(promoId.intValue()!=goodsModel.getPromoModel().getId()){
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
            }else if(goodsModel.getPromoModel().getStatus().intValue()!=2){
                //校验活动是否进行中
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动未开始");
            }
        }
        //落单减库存或者支付减库存
        boolean result =  goodsService.decStock(goodsId,amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setGoodsId(goodsId);
        orderModel.setAmount(amount);
        orderModel.setPromoId(promoId);
        if(promoId!=null){
            orderModel.setGoodsPrice(goodsModel.getPromoModel().getPromoGoodsPrice());
        }else{
            orderModel.setGoodsPrice(goodsModel.getPrice());
        }
        orderModel.setOrderPrice(orderModel.getGoodsPrice().multiply(new BigDecimal(amount)));
        orderModel.setOrderId(generateOrderId());
        OrderInfoDO orderInfoDO = convertDOFromModel(orderModel);
//        生成交易流水
        orderInfoDOMapper.insertSelective(orderInfoDO);
        goodsService.increaseSales(goodsId,amount);
        return orderModel;
    }
//    让生成id在事务之外
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String generateOrderId(){
        StringBuilder stringBuilder = new StringBuilder();
        //订单号16位 前八位时间信息 年月日 20200222
        LocalDateTime now = LocalDateTime.now();
        String nowDate =  now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        // 中间6位自增
        //获取当前sequence
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKey(sequenceDO);
        //sequence补零
        String sequenceStr = String.format("%06d",sequence);
        stringBuilder.append(sequenceStr);
        // 最后两位 分库分表 (水平拆分)
        stringBuilder.append("00");
        return stringBuilder.toString();
    }
    private OrderInfoDO convertDOFromModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        BeanUtils.copyProperties(orderModel,orderInfoDO);
        return orderInfoDO;
    }

}
