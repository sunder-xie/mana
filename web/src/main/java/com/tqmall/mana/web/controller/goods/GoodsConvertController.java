package com.tqmall.mana.web.controller.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.goods.GoodsBasicBO;
import com.tqmall.mana.biz.manager.goods.GoodsConvertBiz;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhouheng on 17/3/11.
 */
@RequestMapping("goods")
@Controller
public class GoodsConvertController extends BaseController {

    @Autowired
    private GoodsConvertBiz goodsConvertBiz;


    @RequestMapping("getGoodsConvert")
    @ResponseBody
    public Result<List<GoodsBasicBO>> goodsConvert(String q){

        return goodsConvertBiz.getGoodsConvertList(q);

    }

}
