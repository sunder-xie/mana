package com.tqmall.mana.dao.mapper.sys;

import com.tqmall.mana.beans.entity.sys.ManaSysResourceDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManaSysResourceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManaSysResourceDO record);

    ManaSysResourceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManaSysResourceDO record);

    /** 查询用户资源权限 */
    List<String> selectUserPermissions(String userName);

    List<ManaSysResourceDO> selectByPid(Integer pid);

    List<ManaSysResourceDO> selectAll();

    List<ManaSysResourceDO> getRoleResource(Integer roleId);

    List<ManaSysResourceDO> getUserMenuResource(String userName);

    int logicDeleteByPid(@Param("pid")Integer pid, @Param("operator")String operator);

}