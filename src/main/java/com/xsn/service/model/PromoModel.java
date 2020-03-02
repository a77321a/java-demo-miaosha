package com.xsn.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class PromoModel {
    private Integer id;
    //秒杀活动名称
    private String promoName;
    //秒杀状态 1未开始 2进行中 3已结束
    private Integer status;
    //秒杀开始时间
    private DateTime startTime;
    //秒杀结束时间
    private DateTime endTime;
    //秒杀活动适用商品
    private Integer goodsId;
    //秒杀活动的商品价格
    private BigDecimal promoGoodsPrice;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getPromoGoodsPrice() {
        return promoGoodsPrice;
    }

    public void setPromoGoodsPrice(BigDecimal promoGoodsPrice) {
        this.promoGoodsPrice = promoGoodsPrice;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }
}
