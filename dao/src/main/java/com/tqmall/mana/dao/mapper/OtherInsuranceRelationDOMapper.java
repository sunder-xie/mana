package com.tqmall.mana.dao.mapper;

import com.tqmall.mana.beans.entity.insurance.OtherInsuranceRelationDO;

import java.util.List;

public interface OtherInsuranceRelationDOMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(OtherInsuranceRelationDO record);

    OtherInsuranceRelationDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OtherInsuranceRelationDO record);

    List<OtherInsuranceRelationDO> selectAll();
}