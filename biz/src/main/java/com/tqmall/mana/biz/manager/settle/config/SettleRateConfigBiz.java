package com.tqmall.mana.biz.manager.settle.config;

import com.tqmall.mana.beans.entity.settle.SettleRateConfigDO;
import com.tqmall.mana.beans.param.settle.RateConfigQueryPO;

import java.util.List;

/**
 * Created by huangzhangting on 17/3/11.
 */
public interface SettleRateConfigBiz {

    List<SettleRateConfigDO> getAll(RateConfigQueryPO queryPO);

}
