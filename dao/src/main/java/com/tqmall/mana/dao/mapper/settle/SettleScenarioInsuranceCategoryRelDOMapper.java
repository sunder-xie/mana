package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleScenarioInsuranceCategoryRelDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleScenarioInsuranceCategoryRelDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleScenarioInsuranceCategoryRelDO record);

    int insertSelective(SettleScenarioInsuranceCategoryRelDO record);

    SettleScenarioInsuranceCategoryRelDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleScenarioInsuranceCategoryRelDO record);

    int updateByPrimaryKey(SettleScenarioInsuranceCategoryRelDO record);

    List<SettleScenarioInsuranceCategoryRelDO> getAllScenarioTypeByCompany(@Param("companyId")Integer companyId);
}