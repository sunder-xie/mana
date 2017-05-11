package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.BO.settle.CheckSettleApplyRangeConfigBO;
import com.tqmall.mana.beans.BO.settle.DeleteRegionConfigBO;
import com.tqmall.mana.beans.entity.settle.SettleShopRuleRegionConfigDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleShopRuleRegionConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleShopRuleRegionConfigDO record);

    SettleShopRuleRegionConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleShopRuleRegionConfigDO record);


    int batchInsert(List<SettleShopRuleRegionConfigDO> list);

    /**
     * 检查地区是否配置过结算规则
     * @param configBO
     * @return
     */
    List<String> checkRegionConfig(CheckSettleApplyRangeConfigBO configBO);


    List<SettleShopRuleRegionConfigDO> selectByRuleIds(@Param("ruleIds")List<Integer> ruleIds);


    int deleteBySettleRuleId(DeleteRegionConfigBO configBO);

    List<String> selectAvailableRegionList(@Param("regionParentCode") String regionParentCode,@Param("listIds")List<Integer> listIds);

}