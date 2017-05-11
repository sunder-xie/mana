package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleInsuranceCategoryMatchRegularDO;

public interface SettleInsuranceCategoryMatchRegularDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleInsuranceCategoryMatchRegularDO record);

    int insertSelective(SettleInsuranceCategoryMatchRegularDO record);

    SettleInsuranceCategoryMatchRegularDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleInsuranceCategoryMatchRegularDO record);

    int updateByPrimaryKey(SettleInsuranceCategoryMatchRegularDO record);
}