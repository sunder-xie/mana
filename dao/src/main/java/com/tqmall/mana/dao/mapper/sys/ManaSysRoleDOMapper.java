package com.tqmall.mana.dao.mapper.sys;

import com.tqmall.mana.beans.entity.sys.ManaSysRoleDO;

import java.util.List;

public interface ManaSysRoleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaSysRoleDO record);

    ManaSysRoleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaSysRoleDO record);

    List<ManaSysRoleDO> selectAll();
}