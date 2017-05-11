package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleInsuranceCompanyRuleItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SettleInsuranceCompanyRuleItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleInsuranceCompanyRuleItemDO record);

    int insertSelective(SettleInsuranceCompanyRuleItemDO record);

    SettleInsuranceCompanyRuleItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleInsuranceCompanyRuleItemDO record);

    int updateByPrimaryKey(SettleInsuranceCompanyRuleItemDO record);

    int batchInsert(List<SettleInsuranceCompanyRuleItemDO> list);

    int batchUpdate(SettleInsuranceCompanyRuleItemDO list);

    int deleteRuleItemBySettleRuleId(@Param("settleRuleId") Integer settleRuleId);

    Integer selectCountBySettleRuleIds(@Param("settleRuleIdList") List<Integer> settleRuleIdList);

    List<SettleInsuranceCompanyRuleItemDO> selectRuleItemBysettleRuleId(@Param("settleRuleIdList")List<Integer> settleRuleIdList);

    List<SettleInsuranceCompanyRuleItemDO> selectRuleItemBySettleRuleIdAndScenarioType(@Param("settleRuleId")Integer settleRuleId,@Param("scenarioType")Integer scenarioType);
}