package com.tqmall.mana.dao.mapper.insurance;

import com.tqmall.mana.beans.entity.insurance.ManaInsuranceFormSyncLogDO;
import com.tqmall.mana.beans.param.CommonPageParam;

import java.util.List;

public interface ManaInsuranceFormSyncLogDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaInsuranceFormSyncLogDO record);

    ManaInsuranceFormSyncLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaInsuranceFormSyncLogDO record);

    List<ManaInsuranceFormSyncLogDO> selectInsuranceFormList(CommonPageParam commonPageParam);

}