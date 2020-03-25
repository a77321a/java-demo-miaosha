package com.xsn.service;

import com.xsn.service.model.PromoModel;

public interface PromoService {
    //根据goodsId 获取即将进行或者正在进行的秒杀活动信息
    PromoModel getPromoByGoodsId(Integer goodsId);
    void publishPromo(Integer id);
}
