package com.tqmall.mana.test.ext;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.beans.param.ShopQueryPO;
import com.tqmall.mana.biz.manager.UcShop.UcShopBiz;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.external.dubbo.uc.ExtShopInfoService;
import com.tqmall.mana.test.BaseTest;
import com.tqmall.ucenter.object.result.account.AccountDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by huangzhangting on 17/1/23.
 */
public class ExtShopInfoServiceTest extends BaseTest {
    @Autowired
    private ExtShopInfoService extShopInfoService;
    @Autowired
    private UcShopBiz ucShopBiz;


    @Test
    public void test(){
        Integer shopId = 274223;
        List<AccountDTO> list = extShopInfoService.getAccountListByShopId(shopId);
        System.out.println(JsonUtil.objectToStr(list));
    }

    @Test
    public void test2(){
        ShopQueryPO queryPO = new ShopQueryPO();
        queryPO.setQueryStr("云修");
        PagingResult<SimpleShopBO> result = ucShopBiz.getSimpleShopList(queryPO);
        System.out.println(JsonUtil.objectToStr(result));
    }
}
