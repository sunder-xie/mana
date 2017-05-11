package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.BO.settle.DeleteShopRuleItemBO;
import com.tqmall.mana.beans.entity.settle.SettleShopRuleItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleShopRuleItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleShopRuleItemDO record);

    SettleShopRuleItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleShopRuleItemDO record);


    int batchInsert(List<SettleShopRuleItemDO> list);

    /**
     * 通过组配置id查询组组配置是否使用中
     *
     * @param settleConfigBasicId
     * @return
     */
    Integer selectShopItemCountByBasicId(@Param("settleConfigBasicId") Integer settleConfigBasicId);


    List<SettleShopRuleItemDO> selectByRuleIds(@Param("ruleIds")List<Integer> ruleIds);

    List<SettleShopRuleItemDO> selectByDO(SettleShopRuleItemDO searchDO);

    int deleteItemBySettleRuleId(DeleteShopRuleItemBO itemBO);

    List<Integer> selectItemIdListByRuleId(Integer ruleId);

    int batchDeleteShopRuleItemByIds(@Param("shopRuleIds")List<Integer> shopRuleIds);

}