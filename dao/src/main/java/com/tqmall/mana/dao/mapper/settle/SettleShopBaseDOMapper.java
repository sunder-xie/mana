package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleShopBaseDO;

import java.util.List;

public interface SettleShopBaseDOMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(SettleShopBaseDO record);

    SettleShopBaseDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleShopBaseDO record);





    /*=======by zxg 17.01.17=========*/
    List<SettleShopBaseDO> selectByDO(SettleShopBaseDO searchDO);
}