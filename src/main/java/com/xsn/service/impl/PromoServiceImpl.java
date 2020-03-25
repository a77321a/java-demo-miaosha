package com.xsn.service.impl;

import com.xsn.dao.PromoDOMapper;
import com.xsn.dataobject.PromoDO;
import com.xsn.service.GoodsService;
import com.xsn.service.PromoService;
import com.xsn.service.model.GoodsModel;
import com.xsn.service.model.PromoModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PromoModel getPromoByGoodsId(Integer goodsId) {
        PromoDO promoDO = promoDOMapper.selectByGoodsId(goodsId);
        PromoModel promoModel = convertModelFromDO(promoDO);
        if(promoModel==null){
            return null;
        }
        //判断秒杀活动是否开始或者已经开始
        LocalDateTime now = LocalDateTime.now();
        if(promoModel.getStartTime().isAfter(now)){
            promoModel.setStatus(1);
        }else if(promoModel.getEndTime().isBefore(now)){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }
        return promoModel;
    }

    @Override
    public void publishPromo(Integer id) {
        //通过活动id获取活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(id);
        if(promoDO.getGoodsId()==null|| promoDO.getGoodsId().intValue()==0){
            return;
        }
        GoodsModel goodsModel = goodsService.getGoodsDetail(promoDO.getGoodsId());
        //将库存同步到redis内
        redisTemplate.opsForValue().set("promo_goods_stock"+goodsModel.getId(),goodsModel.getStock());
    }

    private PromoModel convertModelFromDO(PromoDO promoDO){
        if(promoDO==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setStartTime(promoDO.getStartTime());
        promoModel.setEndTime(promoDO.getEndTime());
        return promoModel;
    }
}
