package com.tqmall.mana.biz.manager.sys;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.sys.*;
import com.tqmall.mana.beans.VO.ModifyRoleVO;
import com.tqmall.mana.beans.VO.ZTree;
import com.tqmall.mana.beans.entity.sys.ManaSysResourceDO;
import com.tqmall.mana.beans.entity.sys.ManaSysRoleDO;
import com.tqmall.mana.beans.entity.sys.ManaSysRoleResourceDO;
import com.tqmall.mana.beans.entity.sys.ManaSysUserRoleDO;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.redis.RedisClientTemplate;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.dao.mapper.sys.ManaSysResourceDOMapper;
import com.tqmall.mana.dao.mapper.sys.ManaSysRoleDOMapper;
import com.tqmall.mana.dao.mapper.sys.ManaSysRoleResourceDOMapper;
import com.tqmall.mana.dao.mapper.sys.ManaSysUserRoleDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by huangzhangting on 16/12/16.
 */
@Slf4j
@Service
public class SysBizImpl implements SysBiz {
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private ManaSysResourceDOMapper sysResourceDOMapper;
    @Autowired
    private ManaSysRoleDOMapper sysRoleDOMapper;
    @Autowired
    private ManaSysRoleResourceDOMapper roleResourceDOMapper;
    @Autowired
    private ManaSysUserRoleDOMapper userRoleDOMapper;


    /** 清理用户资源权限缓存 */
    private void clearUserPermissionsCache(String userName){
        List<String> list = new ArrayList<>();
        list.add(String.format(RedisKeyBean.USER_PERMISSIONS_KEY, userName));
        list.add(String.format(RedisKeyBean.USER_MENUS_KEY, userName));

        redisClient.delKeys(list.toArray(new String[list.size()]));
    }
    private void clearMultiUserPermissionsCache(List<String> userNameList){
        int size = userNameList.size();
        if(size == 0){
            return;
        }
        List<String> list = new ArrayList<>();
        for(int i=0; i<size; i++){
            String userName = userNameList.get(i);
            list.add(String.format(RedisKeyBean.USER_PERMISSIONS_KEY, userName));
            list.add(String.format(RedisKeyBean.USER_MENUS_KEY, userName));
        }
        redisClient.delKeys(list.toArray(new String[list.size()]));
    }


    @Override
    public Set<String> getUserPermissions(String userName) {
        if(StringUtils.isEmpty(userName)){
            return new HashSet<>();
        }

        //先从缓存获取
        String key = String.format(RedisKeyBean.USER_PERMISSIONS_KEY, userName);
        String redisStr = redisClient.get(key);
        if(redisClient.isNone(redisStr)){
            return new HashSet<>();
        }
        if(redisStr != null){
            return JsonUtil.strToCollection(redisStr, Set.class, String.class);
        }

        //再从数据库获取
        List<String> list = sysResourceDOMapper.selectUserPermissions(userName);
        if(list.isEmpty()){
            redisClient.setNone(key, RedisKeyBean.RREDIS_EXP_MINUTES);
            return new HashSet<>();
        }

        //处理权限字符串
        Set<String> permissions = new HashSet<>();
        for(String str : list){
            if(!StringUtils.isEmpty(str)){
                permissions.add(str);
            }
        }

        if(permissions.isEmpty()){
            redisClient.setNone(key, RedisKeyBean.RREDIS_EXP_MINUTES);
        }else{
            redisClient.lazySet(key, permissions, RedisKeyBean.RREDIS_EXP_DAY);
        }

        return permissions;
    }


    private List<ManaSysResourceDO> getUserMenuResourceDO(String userName) {
        String key = String.format(RedisKeyBean.USER_MENUS_KEY, userName);
        String redisStr = redisClient.get(key);
        if(redisClient.isNone(redisStr)){
            return new ArrayList<>();
        }
        if(redisStr!=null){
            return JsonUtil.strToList(redisStr, ManaSysResourceDO.class);
        }
        List<ManaSysResourceDO> resourceDOs = sysResourceDOMapper.getUserMenuResource(userName);
        if(resourceDOs.isEmpty()){
            redisClient.setNone(key);
        }else{
            redisClient.lazySet(key, resourceDOs, RedisKeyBean.RREDIS_EXP_DAY);
        }
        return resourceDOs;
    }

    @Override
    public List<ResourceBO> getUserMenuResource(String userName, Integer menuId) {
        List<ResourceBO> resourceBOList = new ArrayList<>();
        if(StringUtils.isEmpty(userName)){
            return resourceBOList;
        }

        List<ManaSysResourceDO> sysResourceDOList = getUserMenuResourceDO(userName);
        if(sysResourceDOList.isEmpty()){
            return resourceBOList;
        }

        Set<Integer> idSet = new HashSet<>();
        Map<Integer, List<ResourceBO>> parentIdMap = new LinkedHashMap<>();
        for(ManaSysResourceDO sysResourceDO : sysResourceDOList){
            if(!idSet.add(sysResourceDO.getId())){
                continue;
            }
            ResourceBO resourceBO = BdUtil.do2bo(sysResourceDO, ResourceBO.class);
            resourceBOList.add(resourceBO);

            if(resourceBO.getId().equals(menuId)){
                resourceBO.setSelected(true);
            }

            Integer pid = resourceBO.getParentId();
            List<ResourceBO> children = parentIdMap.get(pid);
            if(children==null){
                children = new ArrayList<>();
                parentIdMap.put(pid, children);
            }
            children.add(resourceBO);
        }

        //组装子级资源
        for(ResourceBO resource : resourceBOList){
            resource.setChildren(parentIdMap.remove(resource.getId()));
        }

        //取出最高级别资源
        List<ResourceBO> list = parentIdMap.get(0);
        Collections.sort(list, new Comparator<ResourceBO>() {
            @Override
            public int compare(ResourceBO o1, ResourceBO o2) {
                return o1.getResourceSort().compareTo(o2.getResourceSort());
            }
        });
        return list;
    }

    @Override
    public Result<ManaSysResourceDO> getResourceById(Integer id) {
        if(id==null || id<1){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        ManaSysResourceDO sysResourceDO = sysResourceDOMapper.selectByPrimaryKey(id);
        if(sysResourceDO==null){
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        return ResultUtil.successResult(sysResourceDO);
    }

    @Override
    public Result addResource(ManaSysResourceDO resourceDO) {
        if(resourceDO==null || StringUtils.isEmpty(resourceDO.getResourceName())){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        resourceDO.setCreator(shiroBiz.getCurrentUserRealName());
        resourceDO.setGmtCreate(new Date());

        if(resourceDO.getParentId() != null && resourceDO.getParentId() < 0){
            resourceDO.setParentId(0);
        }

        return ResultUtil.successResult(sysResourceDOMapper.insertSelective(resourceDO));
    }

    @Override
    public Result modifyResource(ManaSysResourceDO resourceDO) {
        if(resourceDO==null || resourceDO.getId()==null){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        resourceDO.setModifier(shiroBiz.getCurrentUserRealName());
        resourceDO.setGmtModified(new Date());

        if(resourceDO.getParentId() != null && resourceDO.getParentId() < 0){
            resourceDO.setParentId(0);
        }

        return ResultUtil.successResult(sysResourceDOMapper.updateByPrimaryKeySelective(resourceDO));
    }

    @Override
    public Result deleteResource(Integer id) {
        if(id==null || id<1){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        String operator = shiroBiz.getCurrentUserRealName();
        ManaSysResourceDO resourceDO = new ManaSysResourceDO();
        resourceDO.setId(id);
        resourceDO.setIsDeleted("Y");
        resourceDO.setModifier(operator);
        resourceDO.setGmtModified(new Date());

        int num = sysResourceDOMapper.updateByPrimaryKeySelective(resourceDO);
        if(num>0) {
            //清理权限缓存
            Set<String> userNameSet = roleResourceDOMapper.getUserNames(id);
            if(!userNameSet.isEmpty()) {
                clearMultiUserPermissionsCache(new ArrayList<>(userNameSet));
            }
            //删除角色关系数据
            roleResourceDOMapper.deleteByResourceId(id);
            //逻辑删除下级资源
            sysResourceDOMapper.logicDeleteByPid(id, operator);
        }

        return ResultUtil.successResult(num);
    }

    @Override
    public List<ManaSysResourceDO> getResourcesByPid(Integer pid) {
        if(pid==null || pid<0){
            return new ArrayList<>();
        }
        return sysResourceDOMapper.selectByPid(pid);
    }

    @Override
    public List<ManaSysResourceDO> getAllResources() {
        List<ManaSysResourceDO> list = sysResourceDOMapper.selectAll();
        if(!list.isEmpty()){
            Collections.sort(list, new Comparator<ManaSysResourceDO>() {
                @Override
                public int compare(ManaSysResourceDO o1, ManaSysResourceDO o2) {
                    return o1.getResourceSort().compareTo(o2.getResourceSort());
                }
            });
        }
        return list;
    }

    @Override
    public List<ResourceBO> getResourceBOListTree() {
        List<ManaSysResourceDO> sysResourceDOList = getAllResources();

        List<ResourceBO> resourceBOList = new ArrayList<>();
        if(sysResourceDOList.isEmpty()){
            return resourceBOList;
        }

        Map<Integer, List<ResourceBO>> parentIdMap = new LinkedHashMap<>();
        for(ManaSysResourceDO sysResourceDO : sysResourceDOList){
            ResourceBO resourceBO = BdUtil.do2bo(sysResourceDO, ResourceBO.class);
            switch (resourceBO.getResourceType()){
                case 0: resourceBO.setTypeString("菜单"); break;
                case 1: resourceBO.setTypeString("<span style='color:red'>操作</span>"); break;
                default: break;
            }
            resourceBOList.add(resourceBO);

            Integer pid = resourceBO.getParentId();
            List<ResourceBO> children = parentIdMap.get(pid);
            if(children==null){
                children = new ArrayList<>();
                parentIdMap.put(pid, children);
            }
            children.add(resourceBO);
        }

        //组装子级资源
        for(ResourceBO resource : resourceBOList){
            resource.setChildren(parentIdMap.remove(resource.getId()));
        }

        //取出最高级别资源
        return parentIdMap.get(0);
    }

    @Override
    public List<Map<String, Object>> getParentResourceTree(Integer id) {
        List<ManaSysResourceDO> sysResourceDOList = getAllResources();
        if(sysResourceDOList.isEmpty()){
            return new ArrayList<>();
        }

        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> parentIdMap = new LinkedHashMap<>();
        for(ManaSysResourceDO resource : sysResourceDOList){
            //如果是修改，则不添加自身，设置下级节点时等效于过滤下级节点
            if(resource.getId().equals(id)){
                continue;
            }
            String pid = resource.getParentId().toString();
            Map<String, Object> data = new HashMap<>();
            data.put("id", resource.getId());
            data.put("pid", pid);
            data.put("text", resource.getResourceName());
            dataList.add(data);

            List<Map<String, Object>> children = parentIdMap.get(pid);
            if(children==null){
                children = new ArrayList<>();
                parentIdMap.put(pid, children);
            }
            children.add(data);
        }

        //组装子级
        for(Map<String, Object> dataMap : dataList){
            dataMap.put("children", parentIdMap.remove(dataMap.get("id").toString()));
        }

        //取出第一级资源
        return parentIdMap.get("0");
    }

    @Override
    public List<ZTree> getAllResourcesZTree() {
        List<ManaSysResourceDO> resourceDOList = getAllResources();
        List<ZTree> zTreeList = new ArrayList<>();
        if(resourceDOList.isEmpty()){
            return zTreeList;
        }

        for(ManaSysResourceDO resourceDO : resourceDOList){
            ZTree zTree = new ZTree();
            zTree.setId(resourceDO.getId());
            zTree.setPid(resourceDO.getParentId());
            zTree.setName(resourceDO.getResourceName());
            if(resourceDO.getParentId() > 0){ //非一级资源默认不展开
                zTree.setOpen(false);
            }

            zTreeList.add(zTree);
        }

        return zTreeList;
    }

    @Override
    public List<ZTree> getAllResourcesZTreeForRole(Integer roleId) {
        if(roleId==null || roleId<1){
            return getAllResourcesZTree();
        }
        Set<Integer> roleResourceIds = roleResourceDOMapper.getRoleResourceIds(roleId);
        if(roleResourceIds.isEmpty()){
            return getAllResourcesZTree();
        }

        List<ManaSysResourceDO> resourceDOList = getAllResources();
        List<ZTree> zTreeList = new ArrayList<>();
        if(resourceDOList.isEmpty()){
            return zTreeList;
        }

        for(ManaSysResourceDO resourceDO : resourceDOList){
            ZTree zTree = new ZTree();
            zTree.setId(resourceDO.getId());
            zTree.setPid(resourceDO.getParentId());
            zTree.setName(resourceDO.getResourceName());
            if(resourceDO.getParentId() > 0){ //非一级资源默认不展开
                zTree.setOpen(false);
            }
            if(roleResourceIds.contains(resourceDO.getId())){
                zTree.setChecked(true);
            }

            zTreeList.add(zTree);
        }

        return zTreeList;
    }

    @Override
    public Result<List<ZTree>> getRoleResourceZTree(Integer roleId) {
        if(roleId==null || roleId<1){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        List<ManaSysResourceDO> resourceDOList = sysResourceDOMapper.getRoleResource(roleId);
        if(resourceDOList.isEmpty()){
            return ResultUtil.errorResult("", "未配置资源");
        }

        List<ZTree> zTreeList = new ArrayList<>();
        for(ManaSysResourceDO resourceDO : resourceDOList){
            ZTree zTree = new ZTree(false);
            zTree.setId(resourceDO.getId());
            zTree.setPid(resourceDO.getParentId());
            zTree.setName(resourceDO.getResourceName());

            zTreeList.add(zTree);
        }

        return ResultUtil.successResult(zTreeList);
    }

    @Override
    public Result addRole(AddRoleBO addRoleBO) {
        if(addRoleBO==null || StringUtils.isEmpty(addRoleBO.getRoleName())){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        ManaSysRoleDO roleDO = BdUtil.do2bo(addRoleBO, ManaSysRoleDO.class);
        roleDO.setGmtCreate(new Date());
        roleDO.setCreator(shiroBiz.getCurrentUserRealName());
        sysRoleDOMapper.insertSelective(roleDO);

        List<Integer> resourceIds = addRoleBO.getResourceIds();
        if(!CollectionUtils.isEmpty(resourceIds)){
            ModifyRoleResourceBO modifyRoleResourceBO = new ModifyRoleResourceBO();
            modifyRoleResourceBO.setRoleId(roleDO.getId());
            modifyRoleResourceBO.setResourceIds(resourceIds);
            modifyRoleResourceBO.setModify(false);

            modifyRoleResource(modifyRoleResourceBO);
        }

        return ResultUtil.successResult(roleDO.getId());
    }

    @Override
    public Result modifyRole(ModifyRoleBO modifyRoleBO) {
        if(modifyRoleBO==null || modifyRoleBO.getId()==null){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        ManaSysRoleDO roleDO = BdUtil.do2bo(modifyRoleBO, ManaSysRoleDO.class);
        roleDO.setGmtModified(new Date());
        roleDO.setModifier(shiroBiz.getCurrentUserRealName());
        sysRoleDOMapper.updateByPrimaryKeySelective(roleDO);

        if(!modifyRoleBO.isSimpleModify()){
            ModifyRoleResourceBO modifyRoleResourceBO = new ModifyRoleResourceBO();
            modifyRoleResourceBO.setRoleId(roleDO.getId());
            modifyRoleResourceBO.setResourceIds(modifyRoleBO.getResourceIds());
            modifyRoleResourceBO.setModify(true);

            modifyRoleResource(modifyRoleResourceBO);
        }

        return ResultUtil.successResult(1);
    }

    @Override
    public Result deleteRole(Integer id) {
        if(id==null || id<1){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        ManaSysRoleDO roleDO = new ManaSysRoleDO();
        roleDO.setId(id);
        roleDO.setIsDeleted("Y");
        roleDO.setGmtModified(new Date());
        roleDO.setModifier(shiroBiz.getCurrentUserRealName());

        int num = sysRoleDOMapper.updateByPrimaryKeySelective(roleDO);
        if(num>0){
            //删除资源角色关系
            ModifyRoleResourceBO modifyRoleResourceBO = new ModifyRoleResourceBO();
            modifyRoleResourceBO.setRoleId(id);
            modifyRoleResource(modifyRoleResourceBO);
            //删除用户角色关系（必须在上面的操作之后，因为删除缓存需要用到关系表）
            userRoleDOMapper.deleteByRoleId(id);
        }

        return ResultUtil.successResult(num);
    }

    //fixme
    @Override
    public PagingResult<ManaSysRoleDO> getRolePage(SearchRoleBO searchRoleBO) {

        return null;
    }

    @Override
    public Result<List<ManaSysRoleDO>> getAllRoles() {
        List<ManaSysRoleDO> list = sysRoleDOMapper.selectAll();
        if(list.isEmpty()){
            return ResultUtil.errorResult("", "未添加角色");
        }
        Collections.sort(list, new Comparator<ManaSysRoleDO>() {
            @Override
            public int compare(ManaSysRoleDO o1, ManaSysRoleDO o2) {
                return o1.getRoleSort().compareTo(o2.getRoleSort());
            }
        });
        return ResultUtil.successResult(list);
    }

    @Override
    public Result<ModifyRoleVO> getModifyRoleVO(Integer id) {
        if(id==null || id<1){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        ManaSysRoleDO roleDO = sysRoleDOMapper.selectByPrimaryKey(id);
        if(roleDO==null){
            return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
        }
        ModifyRoleVO modifyRoleVO = BdUtil.do2bo(roleDO, ModifyRoleVO.class);
        modifyRoleVO.setResourceList(getAllResourcesZTreeForRole(id));

        return ResultUtil.successResult(modifyRoleVO);
    }

    @Override
    public Result modifyRoleResource(ModifyRoleResourceBO modifyRoleResourceBO) {
        if(modifyRoleResourceBO==null || modifyRoleResourceBO.getRoleId()==null){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        Integer roleId = modifyRoleResourceBO.getRoleId();

        int num = 0;
        if(modifyRoleResourceBO.isModify()){
            //先删除老的资源
            num = roleResourceDOMapper.deleteRoleResource(roleId);
        }

        List<Integer> resourceIds = modifyRoleResourceBO.getResourceIds();
        if(!CollectionUtils.isEmpty(resourceIds)){ //如果为空，则表示删除该角色的全部资源
            //添加新的资源
            List<ManaSysRoleResourceDO> list = new ArrayList<>();
            String name = shiroBiz.getCurrentUserRealName();
            Date date = new Date();
            for(Integer resId : resourceIds){
                ManaSysRoleResourceDO roleResourceDO = new ManaSysRoleResourceDO();
                roleResourceDO.setGmtCreate(date);
                roleResourceDO.setCreator(name);
                roleResourceDO.setRoleId(roleId);
                roleResourceDO.setResourceId(resId);
                list.add(roleResourceDO);
            }
            num = roleResourceDOMapper.batchInsert(list);
        }

        if(modifyRoleResourceBO.isModify()) {
            // 获取拥有该角色的用户，然后清理权限缓存
            List<String> userNameList = userRoleDOMapper.selectRoleUserNames(roleId);
            clearMultiUserPermissionsCache(userNameList);
        }

        return ResultUtil.successResult(num);
    }


    @Override
    public Result modifyUserRole(ModifyUserRoleBO modifyUserRoleBO) {
        if(modifyUserRoleBO==null || StringUtils.isEmpty(modifyUserRoleBO.getUserName())){
            return ResultUtil.errorResult("", "参数不能为空");
        }
        String userName = modifyUserRoleBO.getUserName();

        //先删除老的角色
        int num = userRoleDOMapper.deleteUserRole(userName);

        List<Integer> roleIds = modifyUserRoleBO.getRoleIds();
        if(!CollectionUtils.isEmpty(roleIds)){ //如果为空，则表示，清空该用户的所有角色
            //添加新的角色
            List<ManaSysUserRoleDO> list = new ArrayList<>();
            String name = shiroBiz.getCurrentUserRealName();
            Date date = new Date();
            for(Integer rId : roleIds){
                ManaSysUserRoleDO userRoleDO = new ManaSysUserRoleDO();
                userRoleDO.setGmtCreate(date);
                userRoleDO.setCreator(name);
                userRoleDO.setUserName(userName);
                userRoleDO.setRoleId(rId);

                list.add(userRoleDO);
            }
            userRoleDOMapper.batchInsert(list);
        }

        // 清理该用户的资源权限缓存
        clearUserPermissionsCache(userName);

        return ResultUtil.successResult(num);
    }

    private List<ZTree> getAllRoleZTree(){
        List<ZTree> zTreeList = new ArrayList<>();
        List<ManaSysRoleDO> sysRoleDOList = sysRoleDOMapper.selectAll();
        if(sysRoleDOList.isEmpty()){
            return zTreeList;
        }
        for (ManaSysRoleDO roleDO : sysRoleDOList) {
            ZTree zTree = new ZTree(false);
            zTree.setId(roleDO.getId());
            zTree.setPid(0);
            zTree.setName(roleDO.getRoleName());

            zTreeList.add(zTree);
        }

        return zTreeList;
    }

    @Override
    public List<ZTree> getAllRoleZTreeForUser(String userName) {
        if(StringUtils.isEmpty(userName)){
            return getAllRoleZTree();
        }
        Set<Integer> roleIds = userRoleDOMapper.selectUserRoleIds(userName);
        if(roleIds.isEmpty()){
            return getAllRoleZTree();
        }

        List<ZTree> zTreeList = new ArrayList<>();
        List<ManaSysRoleDO> sysRoleDOList = sysRoleDOMapper.selectAll();
        if(sysRoleDOList.isEmpty()){
            return zTreeList;
        }
        for (ManaSysRoleDO roleDO : sysRoleDOList) {
            ZTree zTree = new ZTree(false);
            zTree.setId(roleDO.getId());
            zTree.setPid(0);
            zTree.setName(roleDO.getRoleName());
            if(roleIds.contains(roleDO.getId())){
                zTree.setChecked(true);
            }

            zTreeList.add(zTree);
        }

        return zTreeList;
    }
}
