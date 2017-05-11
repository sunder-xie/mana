package com.tqmall.mana.dao.mapper.settle.extend;

import com.tqmall.mana.beans.entity.settle.extend.SettleShopStatisticsDO;
import org.apache.ibatis.annotations.Param;

/**
 * Created by huangzhangting on 17/3/6.
 */
public interface SettleShopStatisticsMapper {
    /** 门店服务包数量统计 */
    SettleShopStatisticsDO shopServicePackageStatistics(@Param("shopId")Integer shopId);

    /** 门店结算数据统计 */
    SettleShopStatisticsDO shopSettleFeeStatistics(@Param("shopId")Integer shopId, @Param("settleRuleType")Integer settleRuleType);

}
