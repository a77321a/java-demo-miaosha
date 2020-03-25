package com.xsn.service.impl;

import com.xsn.dao.GoodsDOMapper;
import com.xsn.dao.GoodsStockDOMapper;
import com.xsn.dataobject.GoodsDO;
import com.xsn.dataobject.GoodsStockDO;
import com.xsn.error.BusinessException;
import com.xsn.error.EmBusinessError;
import com.xsn.mq.MqProducer;
import com.xsn.service.GoodsService;
import com.xsn.service.PromoService;
import com.xsn.service.model.GoodsModel;
import com.xsn.service.model.PromoModel;
import com.xsn.validator.ValidResult;
import com.xsn.validator.ValidatorImpl;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private MqProducer mqProducer;
    
    
    
    
    @Autowired
    private ValidatorImpl validatorImpl;
    @Autowired
    private GoodsDOMapper goodsDOMapper;
    @Autowired
    private GoodsStockDOMapper goodsStockDOMapper;
    @Autowired
    private PromoService promoService;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Override
    @Transactional
    public boolean decStock(Integer goodsId,Integer amount) {
        //从redis中扣减库存，生产环境要保证redis性能 
        long result = redisTemplate.opsForValue().increment("promo_goods_stock"+goodsId,amount.intValue()*-1);
        if(result>=0){
            try {
                SendResult sendResult = mqProducer.asyncReduceStock(goodsId,amount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            }
            return true;
        }
        return  false;
    }

    @Override
    @Transactional
    public void increaseSales(Integer goodsId, Integer amount) {
        goodsDOMapper.increaseSales(goodsId,amount);
    }

    @Override
    public GoodsModel getGoodsByIdInCache(Integer goodsId) {
       GoodsModel goodsModel = (GoodsModel) redisTemplate.opsForValue().get("goods_validate_"+goodsId);
       if(goodsModel==null) {
           goodsModel = this.getGoodsDetail(goodsId);
           redisTemplate.opsForValue().set("goods_validate_"+goodsId,goodsModel);
           redisTemplate.expire("goods_validate_"+goodsId,10, TimeUnit.MINUTES);
       }
        return goodsModel;
    }

    @Override
    @Transactional
    public GoodsModel createGoods(GoodsModel goodsModel) throws BusinessException {
//        检验入参
        ValidResult result = validatorImpl.validate(goodsModel);
        if(result.isHsErrs()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

//        转化goodsmodel -》 dataobject
        GoodsDO goodsDO = convertGoodsDOFromModel(goodsModel);
        goodsDOMapper.insertSelective(goodsDO);
        goodsModel.setId(goodsDO.getId());
//        写入数据库
        GoodsStockDO goodsStockDO = convertGoodsStockDOFromModel(goodsModel);
        goodsStockDOMapper.insertSelective(goodsStockDO);
        return getGoodsDetail(goodsModel.getId());
    }

    private GoodsStockDO convertGoodsStockDOFromModel(GoodsModel goodsModel){
        if(goodsModel==null)return null;
        GoodsStockDO goodsStockDO = new GoodsStockDO();
        goodsStockDO.setGoodsId(goodsModel.getId());
        goodsStockDO.setStock(goodsModel.getStock());
        return goodsStockDO;
    }
    private GoodsDO convertGoodsDOFromModel(GoodsModel goodsModel){
        if(goodsModel==null)return null;
        GoodsDO goodsDO = new GoodsDO();
        BeanUtils.copyProperties(goodsModel,goodsDO);
        return goodsDO;
    }
    @Override
    public List<GoodsModel> goodsList() {
        List<GoodsDO> goodsDOList = goodsDOMapper.selectGoodsList();
        List<GoodsModel> goodsModelList =  goodsDOList.stream().map(goods->{
            GoodsStockDO goodsStockDO = goodsStockDOMapper.selectByGoodsId(goods.getId());
            GoodsModel goodsModel = convertModelFromDO(goods,goodsStockDO);
            return goodsModel;
        }).collect(Collectors.toList());
        return goodsModelList;
    }

    @Override
    public GoodsModel getGoodsDetail(Integer id) {
        GoodsDO goodsDO = goodsDOMapper.selectByPrimaryKey(id);
        if(goodsDO==null)
            return null;
       GoodsStockDO goodsStockDO =  goodsStockDOMapper.selectByGoodsId(id);
       GoodsModel goodsModel = convertModelFromDO(goodsDO,goodsStockDO);
       //获取活动商品信息
       PromoModel promoModel = promoService.getPromoByGoodsId(goodsModel.getId());
        if(promoModel!=null && promoModel.getStatus()!=3){
            goodsModel.setPromoModel(promoModel);
        }
       return goodsModel;
//        return
    }
    private GoodsModel convertModelFromDO(GoodsDO goodsDO,GoodsStockDO goodsStockDO){
        if(goodsDO == null) return null;
        GoodsModel goodsModel = new GoodsModel();
        BeanUtils.copyProperties(goodsDO,goodsModel);
        goodsModel.setStock(goodsStockDO.getStock());
        return goodsModel;
    }
}
