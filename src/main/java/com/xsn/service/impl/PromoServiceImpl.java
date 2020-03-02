package com.xsn.service.impl;

import com.xsn.dao.PromoDOMapper;
import com.xsn.dataobject.PromoDO;
import com.xsn.service.PromoService;
import com.xsn.service.model.PromoModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;
    @Override
    public PromoModel getPromoByGoodsId(Integer goodsId) {
        PromoDO promoDO = promoDOMapper.selectByGoodsId(goodsId);
        PromoModel promoModel = convertModelFromDO(promoDO);
        if(promoModel==null){
            return null;
        }
        //判断秒杀活动是否开始或者已经开始
        DateTime now = new DateTime();
        System.out.println(promoModel.getStartTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")));
        if(promoModel.getStartTime().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndTime().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);

        }
        return promoModel;
    }
    private PromoModel convertModelFromDO(PromoDO promoDO){
        if(promoDO==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setStartTime(new DateTime(promoDO.getStartTime()));
        promoModel.setEndTime(new DateTime(promoDO.getEndTime()));
        return promoModel;
    }
}
