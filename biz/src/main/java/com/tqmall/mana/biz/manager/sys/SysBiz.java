package com.tqmall.mana.biz.manager.sys;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.sys.*;
import com.tqmall.mana.beans.VO.ModifyRoleVO;
import com.tqmall.mana.beans.VO.ZTree;
import com.tqmall.mana.beans.entity.sys.ManaSysResourceDO;
import com.tqmall.mana.beans.entity.sys.ManaSysRoleDO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangzhangting on 16/12/16.
 */
public interface SysBiz {
    /**
     * 获取用户资源权限
     * @param userName
     * @return
     */
    Set<String> getUserPermissions(String userName);


    List<ResourceBO> getUserMenuResource(String userName, Integer menuId);

    Result<ManaSysResourceDO> getResourceById(Integer id);

    Result addResource(ManaSysResourceDO resourceDO);

    Result modifyResource(ManaSysResourceDO resourceDO);

    /**
     * TODO 现在只是逻辑删除自身，没有关联删除子级资源，以及其他关联数据，后续再完善
     * @param id
     * @return
     */
    Result deleteResource(Integer id);

    List<ManaSysResourceDO> getResourcesByPid(Integer pid);

    List<ManaSysResourceDO> getAllResources();

    List<ResourceBO> getResourceBOListTree();

    List<Map<String, Object>> getParentResourceTree(Integer id);

    List<ZTree> getAllResourcesZTree();

    List<ZTree> getAllResourcesZTreeForRole(Integer roleId);

    /**
     * 查询角色拥有的资源
     * @param roleId
     * @return
     */
    Result<List<ZTree>> getRoleResourceZTree(Integer roleId);


    /** 角色相关 */
    Result addRole(AddRoleBO addRoleBO);

    Result modifyRole(ModifyRoleBO modifyRoleBO);

    Result deleteRole(Integer id);

    PagingResult<ManaSysRoleDO> getRolePage(SearchRoleBO searchRoleBO);

    Result<List<ManaSysRoleDO>> getAllRoles();

    Result<ModifyRoleVO> getModifyRoleVO(Integer id);

    /** 角色配置资源 */
    Result modifyRoleResource(ModifyRoleResourceBO roleResourceBO);

    /** 用户配置角色 */
    Result modifyUserRole(ModifyUserRoleBO userRoleBO);

    List<ZTree> getAllRoleZTreeForUser(String userName);

}
