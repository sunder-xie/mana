package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleCheckDetailDO;

public interface SettleCheckDetailDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleCheckDetailDO record);

    int insertSelective(SettleCheckDetailDO record);

    SettleCheckDetailDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleCheckDetailDO record);

    int updateByPrimaryKey(SettleCheckDetailDO record);
}