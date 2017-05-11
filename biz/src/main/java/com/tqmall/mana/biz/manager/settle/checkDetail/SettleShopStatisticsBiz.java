package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.mana.beans.BO.settle.SettleShopBO;

/**
 * Created by huangzhangting on 17/3/6.
 */
public interface SettleShopStatisticsBiz {

    /**
     * 查询门店统计数据
     * @param shopId
     * @return
     */
    SettleShopBO getShopStatisticsData(Integer shopId);

}
