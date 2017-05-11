package com.tqmall.mana.web.controller;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.UcShopBO;
import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.beans.param.ShopQueryPO;
import com.tqmall.mana.beans.param.UcShopRequestParam;
import com.tqmall.mana.biz.manager.UcShop.UcShopBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhouheng on 16/12/22.
 */
@RequestMapping("ucShop")
@Controller
public class UcShopController {

    @Autowired
    private UcShopBiz ucShopBiz;

    /**
     * 通过省市区或门店名称获取门店信息
     * @param requestParam
     * @return
     */
    @RequestMapping("getShopListInfo")
    @ResponseBody
    public PagingResult<UcShopBO> getShopListInfo(UcShopRequestParam requestParam){

        return ucShopBiz.getShopListInfo(requestParam);

    }

    @RequestMapping("getSimpleShopList")
    @ResponseBody
    public PagingResult<SimpleShopBO> getSimpleShopList(ShopQueryPO shopQueryPO){
        return ucShopBiz.getSimpleShopList(shopQueryPO);
    }

}
