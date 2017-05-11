package com.tqmall.mana.dao.mapper.insurance;

import com.tqmall.mana.beans.entity.insurance.OtherCityDO;

import java.util.List;

public interface OtherCityDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OtherCityDO record);

    int insertSelective(OtherCityDO record);

    OtherCityDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OtherCityDO record);

    int updateByPrimaryKey(OtherCityDO record);



    List<OtherCityDO> selectAll();
}