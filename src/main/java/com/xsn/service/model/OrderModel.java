package com.xsn.service.model;

import java.math.BigDecimal;

//用户下单交易模型
public class OrderModel {
//    2018291231238y4
    private String orderId;
    private Integer userId;
    //若非空 则是以秒杀活动方式下单
    private Integer promoId;
    
    //购买商品单价,若promoId非空 则是秒杀活动价格
    private BigDecimal goodsPrice;
    private Integer goodsId;
    private Integer amount;
    //购买商品单价,若promoId非空 则是秒杀活动价格

    private BigDecimal orderPrice;

    public String getOrderId() {
        return orderId;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
