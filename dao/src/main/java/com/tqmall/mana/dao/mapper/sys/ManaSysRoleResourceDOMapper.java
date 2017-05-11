package com.tqmall.mana.dao.mapper.sys;

import com.tqmall.mana.beans.entity.sys.ManaSysRoleResourceDO;

import java.util.List;
import java.util.Set;

public interface ManaSysRoleResourceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaSysRoleResourceDO record);

    ManaSysRoleResourceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaSysRoleResourceDO record);

    int deleteRoleResource(Integer roleId);
    int deleteByResourceId(Integer resourceId);

    int batchInsert(List<ManaSysRoleResourceDO> list);

    Set<Integer> getRoleResourceIds(Integer roleId);

    Set<String> getUserNames(Integer resourceId);
}