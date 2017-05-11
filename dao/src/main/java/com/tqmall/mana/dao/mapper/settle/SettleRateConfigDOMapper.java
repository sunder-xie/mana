package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleRateConfigDO;
import com.tqmall.mana.beans.param.settle.RateConfigQueryPO;

import java.util.List;

public interface SettleRateConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleRateConfigDO record);

    SettleRateConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleRateConfigDO record);

    List<SettleRateConfigDO> selectAll(RateConfigQueryPO queryPO);
}