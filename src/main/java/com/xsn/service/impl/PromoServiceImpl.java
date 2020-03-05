package com.xsn.service.impl;

import com.xsn.dao.PromoDOMapper;
import com.xsn.dataobject.PromoDO;
import com.xsn.service.PromoService;
import com.xsn.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
        System.out.println(promoModel.getStartTime()+"------------------------");
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
