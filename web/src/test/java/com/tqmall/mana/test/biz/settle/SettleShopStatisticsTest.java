package com.tqmall.mana.test.biz.settle;

import com.tqmall.mana.beans.BO.settle.SettleShopBO;
import com.tqmall.mana.beans.entity.settle.extend.SettleShopStatisticsDO;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopStatisticsBiz;
import com.tqmall.mana.component.enums.insurance.dict.SettleRuleTypeEnum;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.dao.mapper.settle.extend.SettleShopStatisticsMapper;
import com.tqmall.mana.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 17/3/6.
 */
public class SettleShopStatisticsTest extends BaseTest {
    @Autowired
    private SettleShopStatisticsMapper statisticsMapper;
    @Autowired
    private SettleShopStatisticsBiz shopStatisticsBiz;


    @Test
    public void testDao(){
        Integer shopId = 324846;

        SettleShopStatisticsDO statisticsDO = statisticsMapper.shopServicePackageStatistics(shopId);
        System.out.println(JsonUtil.objectToStr(statisticsDO));

        SettleShopStatisticsDO statisticsDO1 = statisticsMapper.shopSettleFeeStatistics(shopId, SettleRuleTypeEnum.CASH.getCode());
        System.out.println(JsonUtil.objectToStr(statisticsDO1));
    }

    @Test
    public void testBiz(){
        Integer shopId = 324846;
        SettleShopBO settleShopBO = shopStatisticsBiz.getShopStatisticsData(shopId);
        System.out.println(JsonUtil.objectToStr(settleShopBO));
    }

}
