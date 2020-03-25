package com.xsn.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.xsn.controller.viewobject.GoodsVO;
import com.xsn.error.BusinessException;
import com.xsn.response.CommonReturnType;
import com.xsn.service.CacheService;
import com.xsn.service.GoodsService;
import com.xsn.service.PromoService;
import com.xsn.service.model.GoodsModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller("goods")
@RequestMapping("/goods")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class GoodsController extends BaseController  {
    @Autowired
    private GoodsService goodsService;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private CacheService cacheService;
    
    @Autowired
    private PromoService promoService;
    
    //创建商品
    @ResponseBody
    @RequestMapping(value = "/createGoods",method = {RequestMethod.POST})
    public CommonReturnType createGoods(@RequestParam(name = "title")String title,
                                        @RequestParam(name = "price") BigDecimal price,
                                        @RequestParam(name = "description")String description,
                                        @RequestParam(name = "stock")Integer stock,
                                        @RequestParam(name = "imgUrl")String imgUrl) throws BusinessException {
        GoodsModel goodsModel = new GoodsModel();
        goodsModel.setImgUrl(imgUrl);
        goodsModel.setTitle(title);
        goodsModel.setPrice(price);
        goodsModel.setStock(stock);
        goodsModel.setDescription(description);
        GoodsModel goodsModelReturn = goodsService.createGoods(goodsModel);
        GoodsVO goodsVO = convertVOFromModel(goodsModelReturn);
        return CommonReturnType.create(goodsVO);
    }
    
    //商品详情浏览
    @ResponseBody
    @GetMapping("/get")
    public CommonReturnType getGoods(@RequestParam(name = "id")Integer id){
        GoodsModel goodsModel = null;
        //多级缓存 先从本地缓存取，本地没有去redis取
        goodsModel = (GoodsModel) cacheService.getFromCommonCache("goods_"+id);
        if(goodsModel==null){
            //根据商品id到redis内获取
            goodsModel = (GoodsModel) redisTemplate.opsForValue().get("goods_"+id);
            if(goodsModel == null){
                //若redis不存在相应goodsModel，往下走service操作 
                goodsModel = goodsService.getGoodsDetail(id);
                redisTemplate.opsForValue().set("goods_"+id,goodsModel);
                redisTemplate.expire("goods_"+id,10, TimeUnit.MINUTES);
            }
            //填充本地缓存
            cacheService.setCommonCache("goods_"+id,goodsModel);
        }
//        System.out.println(cacheService.getFromCommonCache("goods_"+id).toString());
        GoodsVO goodsVO = convertVOFromModel(goodsModel);
        return CommonReturnType.create(goodsVO);
    }
    
    
    //商品列表浏览
    @ResponseBody
    @RequestMapping(value = "/list")
    public CommonReturnType getGoodsList(){
        List<GoodsModel> goodsModelList = goodsService.goodsList();
        List<GoodsVO> goodsVOList = goodsModelList.stream().map(goods->{
            GoodsVO goodsVO = convertVOFromModel(goods);
            return goodsVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(goodsVOList);
    }

    //发布活动
    @RequestMapping(value = "/publishpromo",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType publishPromo(@RequestParam(name = "id")Integer id){
        promoService.publishPromo(id);
        return CommonReturnType.create(null);
    }
    
    
    //viewobject转换
    private GoodsVO convertVOFromModel(GoodsModel goodsModel){
        if(null == goodsModel) return null;
        GoodsVO goodsVO = new GoodsVO();
        BeanUtils.copyProperties(goodsModel,goodsVO);
        if(goodsModel.getPromoModel()!=null){
            goodsVO.setPromoStatus(goodsModel.getPromoModel().getStatus());
            goodsVO.setPromoId(goodsModel.getPromoModel().getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss"));
            goodsVO.setStartTime(goodsModel.getPromoModel().getStartTime());
            goodsVO.setPromoPrice(goodsModel.getPromoModel().getPromoGoodsPrice());
        }else{
            goodsVO.setPromoStatus(0);
        }
        return goodsVO;
    }
}
