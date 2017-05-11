package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleInsuranceCompanyRuleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SettleInsuranceCompanyRuleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleInsuranceCompanyRuleDO record);

    int insertSelective(SettleInsuranceCompanyRuleDO record);

    SettleInsuranceCompanyRuleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleInsuranceCompanyRuleDO record);

    int updateByPrimaryKey(SettleInsuranceCompanyRuleDO record);

    List<SettleInsuranceCompanyRuleDO> getInsuranceCompanyRuleListByProvinceCode(@Param("provinceCode") String provinceCode);

    Integer getCountByCondation(Map<String, Object> map);

    int deleteInsuranceCompanyRuleById(@Param("id") Integer id);

    List<SettleInsuranceCompanyRuleDO> selectByCityCodeAndProvinceCode(Map<String, Object> map);

    List<SettleInsuranceCompanyRuleDO> selectByCityCods(@Param("cityCodeList") List<String> cityCodeList);
}