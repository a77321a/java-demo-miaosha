package com.xsn.controller;

import com.xsn.controller.viewobject.GoodsVO;
import com.xsn.error.BusinessException;
import com.xsn.response.CommonReturnType;
import com.xsn.service.GoodsService;
import com.xsn.service.model.GoodsModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("goods")
@RequestMapping("/goods")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class GoodsController  {
    @Autowired
    private GoodsService goodsService;
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

//   @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public CommonReturnType getGoods(@RequestParam(name = "id")Integer id){
        GoodsModel goodsModel = goodsService.getGoodsDetail(id);
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
        return goodsVO;
    }
}
