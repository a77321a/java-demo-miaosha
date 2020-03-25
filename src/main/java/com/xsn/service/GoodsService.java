package com.xsn.service;

import com.xsn.error.BusinessException;
import com.xsn.service.model.GoodsModel;

import java.util.List;
public interface GoodsService {
    //创建商品
    GoodsModel createGoods(GoodsModel goodsModel) throws BusinessException;
    //商品列表浏览 参数可能是商品名 活动 分类 等等
    List<GoodsModel> goodsList();
    //商品详情浏览
    GoodsModel getGoodsDetail(Integer id);
    //扣减库存
    boolean decStock(Integer goodsId, Integer amount) throws BusinessException;
    //商品销量增加
    void increaseSales(Integer goodsId, Integer amount);
    //验证商品及活动是否有效
    GoodsModel getGoodsByIdInCache(Integer goodsId);
    
}
