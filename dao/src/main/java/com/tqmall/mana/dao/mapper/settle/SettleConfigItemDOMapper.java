package com.tqmall.mana.dao.mapper.settle;

import com.tqmall.mana.beans.entity.settle.SettleConfigItemDO;
import com.tqmall.mana.beans.entity.settle.extend.SettleConfigItemExtendDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface SettleConfigItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SettleConfigItemDO record);

    int insertSelective(SettleConfigItemDO record);

    SettleConfigItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SettleConfigItemDO record);

    int updateByPrimaryKey(SettleConfigItemDO record);

    List<SettleConfigItemDO> selectItemDOListByBasicId(Integer basicId);


    List<SettleConfigItemExtendDO> selectItemByBasicIds(@Param("basicIds")Set<Integer> basicIds);

}