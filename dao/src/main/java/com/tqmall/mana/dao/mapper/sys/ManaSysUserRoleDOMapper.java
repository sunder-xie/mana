package com.tqmall.mana.dao.mapper.sys;

import com.tqmall.mana.beans.entity.sys.ManaSysUserRoleDO;

import java.util.List;
import java.util.Set;

public interface ManaSysUserRoleDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaSysUserRoleDO record);

    ManaSysUserRoleDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaSysUserRoleDO record);

    int deleteUserRole(String userName);
    int deleteByRoleId(Integer roleId);

    int batchInsert(List<ManaSysUserRoleDO> list);

    List<String> selectRoleUserNames(Integer roleId);

    Set<Integer> selectUserRoleIds(String userName);

}