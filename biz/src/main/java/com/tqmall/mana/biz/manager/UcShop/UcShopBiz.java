package com.tqmall.mana.biz.manager.UcShop;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.UcShopBO;
import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.beans.param.ShopQueryPO;
import com.tqmall.mana.beans.param.UcShopRequestParam;

/**
 * Created by zhouheng on 16/12/22.
 */
public interface UcShopBiz {

    /**
     * 通过省市区或门店名称搜索门店信息
     *
     * @param requestParam
     * @return
     */
    PagingResult<UcShopBO> getShopListInfo(UcShopRequestParam requestParam);


    /**
     * 保险门店查询
     * @param shopQueryPO
     * @return
     */
    PagingResult<SimpleShopBO> getSimpleShopList(ShopQueryPO shopQueryPO);

}
