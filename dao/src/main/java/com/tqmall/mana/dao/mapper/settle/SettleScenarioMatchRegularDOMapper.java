package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleScenarioMatchRegularDO;

public interface SettleScenarioMatchRegularDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleScenarioMatchRegularDO record);

    int insertSelective(SettleScenarioMatchRegularDO record);

    SettleScenarioMatchRegularDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleScenarioMatchRegularDO record);

    int updateByPrimaryKey(SettleScenarioMatchRegularDO record);
}