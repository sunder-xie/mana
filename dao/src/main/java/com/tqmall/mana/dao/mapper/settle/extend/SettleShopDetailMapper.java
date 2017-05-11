package com.tqmall.mana.dao.mapper.settle.extend;

import com.tqmall.mana.beans.entity.settle.extend.ShopSettleDetailDO;
import com.tqmall.mana.beans.entity.settle.extend.ShopSettleStatisticsDO;
import com.tqmall.mana.beans.param.settle.ShopSettleDetailQueryPO;

import java.util.List;

public interface SettleShopDetailMapper {

    /** 奖励金模式数据查询 */
    List<ShopSettleDetailDO> selectShopSettleDetail(ShopSettleDetailQueryPO queryPO);
    int selectShopSettleDetailCount(ShopSettleDetailQueryPO queryPO);
    ShopSettleStatisticsDO selectShopSettleDetailStatistics(ShopSettleDetailQueryPO queryPO);


    /** 服务包模式数据查询 */
    /*
    List<ShopSettleDetailDO> selectShopSettleDetailForPackage(ShopSettleDetailQueryPO queryPO);
    int selectShopSettleDetailForPackageCount(ShopSettleDetailQueryPO queryPO);
    //统计现金（目前只统计商业+交强险返利）
    ShopSettleStatisticsDO selectShopSettleDetailForCashStatistics(ShopSettleDetailQueryPO queryPO);
    //统计服务包工时费
    ShopSettleStatisticsDO selectShopSettleDetailForPackageStatistics(ShopSettleDetailQueryPO queryPO);
    */
    List<ShopSettleDetailDO> selectSettleDataForPackageMode(ShopSettleDetailQueryPO queryPO);
    int selectSettleDataForPackageModeCount(ShopSettleDetailQueryPO queryPO);
    //统计现金（目前只统计商业+交强险返利）
    ShopSettleStatisticsDO selectSettleDataForCashStatistics(ShopSettleDetailQueryPO queryPO);
    //统计服务包工时费
    ShopSettleStatisticsDO selectSettleDataForPackageStatistics(ShopSettleDetailQueryPO queryPO);

}