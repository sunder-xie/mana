package com.tqmall.mana.dao.mapper.insurance;

import com.tqmall.mana.beans.entity.insurance.ManaInsuranceItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManaInsuranceItemDOMapper {

    int insertSelective(ManaInsuranceItemDO record);

    ManaInsuranceItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaInsuranceItemDO record);

    List<ManaInsuranceItemDO> selectByBasicId(Integer basicId);


    int updateByDO(@Param("upDO")ManaInsuranceItemDO upDO,@Param("whereDO")ManaInsuranceItemDO whereDO);

    int insertBatch(@Param("saveList")List<ManaInsuranceItemDO> manaInsuranceItemDOList);
}