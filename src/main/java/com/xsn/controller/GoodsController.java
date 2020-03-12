package com.xsn.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.xsn.controller.viewobject.GoodsVO;
import com.xsn.error.BusinessException;
import com.xsn.response.CommonReturnType;
import com.xsn.service.GoodsService;
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
//        根据商品id到redis内获取
//        GoodsModel goodsModel = new GoodsModel();
//        Gson gson = new Gson();
//        GoodsModel goodsModel = gson.fromJson((JsonElement) redisTemplate.opsForValue().get("goods_"+id),GoodsModel.class);
//        GoodsModel goodsModel = (GoodsModel) redisTemplate.opsForValue().get("goods_"+id);
        GoodsModel goodsModel = (GoodsModel) redisTemplate.opsForValue().get("goods_"+id);
        //若redis不存在相应goodsModel，往下走service操作 
        if(goodsModel == null){
            goodsModel = goodsService.getGoodsDetail(id);
            redisTemplate.opsForValue().set("goods_"+id,goodsModel);
            redisTemplate.expire("goods_"+id,10, TimeUnit.MINUTES);
        }
//        GoodsModel goodsModel = goodsService.getGoodsDetail(id);
        GoodsVO goodsVO = convertVOFromModel(goodsModel);
        return CommonReturnType.create(goodsVO);
    }
    //商品列表浏览
    //商品详情浏览
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
