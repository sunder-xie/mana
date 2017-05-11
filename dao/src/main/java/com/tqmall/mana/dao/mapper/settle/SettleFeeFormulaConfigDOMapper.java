package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleFeeFormulaConfigDO;

import java.util.List;

public interface SettleFeeFormulaConfigDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(SettleFeeFormulaConfigDO record);

    SettleFeeFormulaConfigDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleFeeFormulaConfigDO record);

    List<SettleFeeFormulaConfigDO> selectAll();

    SettleFeeFormulaConfigDO selectByKey(String key);
}