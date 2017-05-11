package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleShopRuleIntroductionDO;

import java.util.List;

public interface SettleShopRuleIntroductionDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleShopRuleIntroductionDO record);

    int insertSelective(SettleShopRuleIntroductionDO record);

    SettleShopRuleIntroductionDO selectByPrimaryKey(Integer id);

    List<SettleShopRuleIntroductionDO> selectIntroductionDOList();

    int updateByPrimaryKeySelective(SettleShopRuleIntroductionDO record);

    int updateByPrimaryKey(SettleShopRuleIntroductionDO record);

    SettleShopRuleIntroductionDO selectByRuleType(Integer ruleType);
}