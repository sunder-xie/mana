package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.mana.beans.BO.settle.SettleShopBO;
import com.tqmall.mana.beans.entity.settle.extend.SettleShopStatisticsDO;
import com.tqmall.mana.component.enums.insurance.dict.SettleRuleTypeEnum;
import com.tqmall.mana.dao.mapper.settle.extend.SettleShopStatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by huangzhangting on 17/3/6.
 */
@Service
@Slf4j
public class SettleShopStatisticsBizImpl implements SettleShopStatisticsBiz {
    @Autowired
    private SettleShopStatisticsMapper shopStatisticsMapper;

    @Override
    public SettleShopBO getShopStatisticsData(Integer shopId) {
        Assert.isTrue(shopId!=null && shopId>0, "参数错误，shopId="+shopId);

        SettleShopBO settleShopBO = new SettleShopBO();
        settleShopBO.setAgentId(shopId);

        //服务包数量统计
        SettleShopStatisticsDO statisticsDO = shopStatisticsMapper.shopServicePackageStatistics(shopId);
        if(statisticsDO!=null){
            settleShopBO.setWaitEffectPackageNum(statisticsDO.getWaitEffectPackageNum());
            settleShopBO.setWaitPackageNum(statisticsDO.getWaitPackageNum());
            settleShopBO.setSendPackageNum(statisticsDO.getSendPackageNum());
            settleShopBO.setReceivePackageNum(statisticsDO.getReceivePackageNum());
        }
        
        //现金结算数据统计
        statisticsDO = shopStatisticsMapper.shopSettleFeeStatistics(shopId, SettleRuleTypeEnum.CASH.getCode());
        if(statisticsDO!=null){
            settleShopBO.setPayableCashAmount(statisticsDO.getPayableCashAmount());
            settleShopBO.setSettledCashAmount(statisticsDO.getSettledCashAmount());
        }

        return settleShopBO;
    }

}
