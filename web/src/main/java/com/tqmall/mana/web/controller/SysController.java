package com.tqmall.mana.web.controller;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.sys.*;
import com.tqmall.mana.beans.VO.ModifyRoleVO;
import com.tqmall.mana.beans.VO.ZTree;
import com.tqmall.mana.beans.entity.sys.ManaSysResourceDO;
import com.tqmall.mana.beans.entity.sys.ManaSysRoleDO;
import com.tqmall.mana.biz.manager.sys.SysBiz;
import com.tqmall.mana.external.dubbo.holy.ExtUserService;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 系统权限表相关操作controller
 * Created by huangzhangting on 16/12/19.
 */
@Controller
@RequestMapping("sys")
public class SysController extends BaseController{
    @Autowired
    private SysBiz sysBiz;
    @Autowired
    private ExtUserService extUserService;


    /** =================== 资源相关代码 ===================== */
    /* 资源管理页面 */
    @RequestMapping("resourcePage")
    public ModelAndView resourcePage(){
        return new ModelAndView("mana/view/sys/resource");
    }


    @RequestMapping("getAllResourcesZTree")
    @ResponseBody
    public List<ZTree> getAllResourcesZTree(){
        return sysBiz.getAllResourcesZTree();
    }


    @RequestMapping("getResourceBOListTree")
    @ResponseBody
    public List<ResourceBO> getResourceBOListTree(){
        return sysBiz.getResourceBOListTree();
    }

    @RequestMapping("getParentResourceTree")
    @ResponseBody
    public List<Map<String, Object>> getParentResourceTree(Integer id){
        return sysBiz.getParentResourceTree(id);
    }

    @RequestMapping(value = "addResource", method = RequestMethod.POST)
    @ResponseBody
    public Result addResource(ManaSysResourceDO resource){
        return sysBiz.addResource(resource);
    }

    @RequestMapping(value = "modifyResource", method = RequestMethod.POST)
    @ResponseBody
    public Result modifyResource(ManaSysResourceDO resource){
        return sysBiz.modifyResource(resource);
    }

    @RequestMapping(value = "deleteResource", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteResource(Integer id){
        return sysBiz.deleteResource(id);
    }

    @RequestMapping("getResourceById")
    @ResponseBody
    public Result<ManaSysResourceDO> getResourceById(Integer id){
        return sysBiz.getResourceById(id);
    }

    @RequestMapping("getRoleResourceZTree")
    @ResponseBody
    public Result<List<ZTree>> getRoleResourceZTree(Integer roleId){
        return sysBiz.getRoleResourceZTree(roleId);
    }


    /** =================== 角色相关代码 ===================== */
    /* 角色管理页面 */
    @RequestMapping("rolePage")
    public ModelAndView rolePage(){
        return new ModelAndView("mana/view/sys/role");
    }


    @RequestMapping(value = "addRole", method = RequestMethod.POST)
    @ResponseBody
    public Result addRole(AddRoleBO addRoleBO){
        return sysBiz.addRole(addRoleBO);
    }

    @RequestMapping("getAllRoles")
    @ResponseBody
    public Result<List<ManaSysRoleDO>> getAllRoles(){
        return sysBiz.getAllRoles();
    }

    @RequestMapping(value = "getRoleForModify", method = RequestMethod.POST)
    @ResponseBody
    public Result<ModifyRoleVO> getRoleForModify(Integer id){
        return sysBiz.getModifyRoleVO(id);
    }

    @RequestMapping(value = "modifyRole", method = RequestMethod.POST)
    @ResponseBody
    public Result modifyRole(ModifyRoleBO modifyRoleBO){
        return sysBiz.modifyRole(modifyRoleBO);
    }

    @RequestMapping(value = "deleteRole", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteRole(Integer id){
        return sysBiz.deleteRole(id);
    }


    @RequestMapping(value = "getUserRoleZTree")
    @ResponseBody
    public List<ZTree> getUserRoleZTree(String userName){
        return sysBiz.getAllRoleZTreeForUser(userName);
    }



    /** =================== 用户相关代码 ===================== */
    /* 用户管理页面 */
    @RequestMapping("userPage")
    public ModelAndView userPage(){
        return new ModelAndView("mana/view/sys/user");
    }

    @RequestMapping("getUserPage")
    @ResponseBody
    public PagingResult<UserBO> getUserPage(SearchUserBO searchUserBO){
        return extUserService.getUserList(searchUserBO);
    }

    @RequestMapping(value = "modifyUserRole", method = RequestMethod.POST)
    @ResponseBody
    public Result modifyUserRole(ModifyUserRoleBO userRoleBO){
        return sysBiz.modifyUserRole(userRoleBO);
    }

}
