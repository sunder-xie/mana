package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.BO.settle.CheckSettleApplyRangeConfigBO;
import com.tqmall.mana.beans.entity.settle.SettleShopRuleShopConfigDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleShopRuleShopConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleShopRuleShopConfigDO record);

    SettleShopRuleShopConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleShopRuleShopConfigDO record);

    /**
     * 检查门店是否已经配置过结算规则
     * @param configBO
     * @return
     */
    Integer checkShopConfig(CheckSettleApplyRangeConfigBO configBO);


    List<SettleShopRuleShopConfigDO> selectByRuleIds(@Param("ruleIds")List<Integer> ruleIds);

}