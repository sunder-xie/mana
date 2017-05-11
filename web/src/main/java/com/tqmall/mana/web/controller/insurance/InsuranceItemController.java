package com.tqmall.mana.web.controller.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.insurance.InsuranceServiceItemBO;
import com.tqmall.mana.beans.BO.insurance.InsuranceServiceItemBriefBO;
import com.tqmall.mana.beans.param.settle.insurance.CheckServiceItemBOParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceItemBiz;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zhouheng on 17/2/20.
 */
@Controller
@RequestMapping("insuranceItem")
public class InsuranceItemController extends BaseController {

    @Autowired
    private InsuranceItemBiz insuranceItemBiz;

    /**
     * 初始化item列表页面
     *
     * @return
     */
    @RequestMapping("itemListInit")
    public ModelAndView itemListInit() {

        ModelAndView modelAndView = new ModelAndView("mana/view/service/itemList");

        return modelAndView;
    }

    /**
     * 初始化新增item页面
     *
     * @return
     */
    @RequestMapping("addItemInit")
    public ModelAndView addItemInit() {

        ModelAndView modelAndView = new ModelAndView("mana/view/service/addItem");

        return modelAndView;
    }

    /**
     * 编辑item页面
     *
     * @return
     */
    @RequestMapping("editItemInit")
    public ModelAndView editItemInit(Integer id) {

        ModelAndView modelAndView = new ModelAndView("mana/view/service/editItem");

        Result<InsuranceServiceItemBO> result = insuranceItemBiz.getServiceItemConfig(id);
        if(result.isSuccess()){
            modelAndView.addObject("insuranceItemBO",result.getData());
        }
        modelAndView.addObject("itemId",id);
        return modelAndView;
    }

    /**
     * 新增item
     *
     * @param serviceItemBO
     * @return
     */
    @RequestMapping("createServiceItemConfig")
    @ResponseBody
    public Result<Boolean> createServiceItemConfig(@RequestBody InsuranceServiceItemBO serviceItemBO) {

        return insuranceItemBiz.createServiceItemConfig(serviceItemBO);

    }

    /**
     * 删除item
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteServiceItem")
    @ResponseBody
    public Result<Boolean> deleteServiceItem(Integer id) {

        return insuranceItemBiz.deleteServiceItem(id);

    }

    /**
     * 更新item
     *
     * @param serviceItemBO
     * @return
     */
    @RequestMapping("updateServiceItemConfig")
    @ResponseBody
    public Result<Boolean> updateServiceItemConfig(@RequestBody InsuranceServiceItemBO serviceItemBO) {

        return insuranceItemBiz.updateServiceItemConfig(serviceItemBO);

    }

    /**
     * 更新item的状态
     * @param id
     * @param status
     * @return
     */
    @RequestMapping("updateServiceItemStatus")
    @ResponseBody
    public Result<Boolean> updateServiceItemStatus(Integer id,Integer status) {

        return insuranceItemBiz.updateServiceItemStatus(id,status);

    }

    /**
     * 获取item分页列表
     *
     * @param checkServiceItemBOParam
     * @return
     */
    @RequestMapping("getServiceItemList")
    @ResponseBody
    public PagingResult<InsuranceServiceItemBO> getServiceItemList(CheckServiceItemBOParam checkServiceItemBOParam) {

        return insuranceItemBiz.getServiceItemList(checkServiceItemBOParam);

    }

    /**
     * 通过id获取item详细信息
     *
     * @param id
     * @return
     */
    @RequestMapping("getServiceItemConfigById")
    @ResponseBody
    public Result<InsuranceServiceItemBO> getServiceItemConfigById(Integer id) {

        return insuranceItemBiz.getServiceItemConfig(id);

    }

    @RequestMapping("getServiceItemNameList")
    @ResponseBody
    public Result<List<InsuranceServiceItemBriefBO>>  getServiceItemNameList(String itemName){

        return insuranceItemBiz.getServiceItemNameList(itemName);

    }


}
