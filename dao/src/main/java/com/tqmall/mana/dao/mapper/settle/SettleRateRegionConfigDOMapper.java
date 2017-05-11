package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleRateRegionConfigDO;

import java.util.Set;

public interface SettleRateRegionConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleRateRegionConfigDO record);

    SettleRateRegionConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleRateRegionConfigDO record);


    Set<Integer> getRateConfigIdsByRegion(String cityCode);
}