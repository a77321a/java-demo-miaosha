package com.xsn.controller.viewobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GoodsVO {
    private Integer id;
    //商品名
    private String title;
    //商品价格
    private BigDecimal price;
    //商品库存
    private Integer stock;
    //商品描述
    private String description;
    //销量
    private Integer sales;
    private String imgUrl;
    //记录商品是否在秒杀活动在， 0：没有秒杀活动，1：代开始 2：进行中：3结束
    private Integer promoStatus;
    //秒杀价格
    private BigDecimal promoPrice;
    //秒杀id
    private Integer promoId;
    //开始时间
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    private LocalDateTime startTime;

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
