package com.tqmall.mana.web.controller.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.insurance.ServicePackageBO;
import com.tqmall.mana.beans.param.settle.insurance.CheckServicePackageBOParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceItemBiz;
import com.tqmall.mana.biz.manager.insurance.InsurancePackageBiz;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhouheng on 17/2/20.
 */
@Controller
@RequestMapping("insurancePackage")
public class InsurancePackageController extends BaseController {

    @Autowired
    private InsurancePackageBiz insurancePackageBiz;
    @Autowired
    private InsuranceItemBiz insuranceItemBiz;

    @RequestMapping("packageListInit")
    public ModelAndView packageListInit() {

        ModelAndView modelAndView = new ModelAndView("mana/view/service/packageList");

        return modelAndView;
    }

    @RequestMapping("addPackageInit")
    public ModelAndView addPackageInit() {

        ModelAndView modelAndView = new ModelAndView("mana/view/service/addPackage");

//        Result<List<InsuranceServiceItemBriefBO>> result = insuranceItemBiz.getServiceItemNameList();
//        if(result.isSuccess()){
//            modelAndView.addObject("itemBriefBOList",result.getData());
//        }
        return modelAndView;
    }

    @RequestMapping("editPackageInit")
    public ModelAndView editPackageInit(Integer packageId) {

        ModelAndView modelAndView = new ModelAndView("mana/view/service/editPackage");
        modelAndView.addObject("packageId",packageId);
//        Result<List<InsuranceServiceItemBriefBO>> result = insuranceItemBiz.getServiceItemNameList();
//        if(result.isSuccess()){
//            modelAndView.addObject("itemBriefBOList",result.getData());
//        }
        Result<ServicePackageBO> packageBOResult = insurancePackageBiz.getServicePackageConfig(packageId);
        if(packageBOResult.isSuccess()){
            modelAndView.addObject("packageBO",packageBOResult.getData());
        }
        return modelAndView;
    }

    @RequestMapping("createServicePackage")
    @ResponseBody
    public Result<Boolean> createServicePackage(@RequestBody ServicePackageBO servicePackageBO) {

        return insurancePackageBiz.createServicePackage(servicePackageBO);

    }

    @RequestMapping("updateServicePackageConfig")
    @ResponseBody
    public Result<Boolean> updateServicePackageConfig(@RequestBody ServicePackageBO servicePackageBO) {

        return insurancePackageBiz.updateServicePackageConfig(servicePackageBO);

    }

    @RequestMapping("getServicePackageList")
    @ResponseBody
    public PagingResult<ServicePackageBO> getServicePackageList(CheckServicePackageBOParam checkServicePackageBOParam) {

        return insurancePackageBiz.getServicePackageList(checkServicePackageBOParam);

    }

    @RequestMapping("getServicePackageConfig")
    @ResponseBody
    public Result<ServicePackageBO> getServicePackageConfig(Integer id) {

        return insurancePackageBiz.getServicePackageConfig(id);

    }

    @RequestMapping("updateServicePackageStatus")
    @ResponseBody
    public Result<Boolean> updateServicePackageStatus(Integer id, Integer packageStatus) {

        return insurancePackageBiz.updateServicePackageStatus(id, packageStatus);

    }

    @RequestMapping("deleteServicePackage")
    @ResponseBody
    public Result<Boolean> deleteServicePackage(Integer id) {

        return insurancePackageBiz.deleteServicePackage(id);

    }


}
