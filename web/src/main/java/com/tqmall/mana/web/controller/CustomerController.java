package com.tqmall.mana.web.controller;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.customer.*;
import com.tqmall.mana.beans.entity.customer.ManaCustomerDO;
import com.tqmall.mana.beans.param.CommonVOPageParam;
import com.tqmall.mana.biz.manager.customer.CommunicateRecordBiz;
import com.tqmall.mana.biz.manager.customer.CustomerBiz;
import com.tqmall.mana.biz.manager.customer.PreferentialLogBiz;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by huangzhangting on 16/12/14.
 */
@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {
    @Autowired
    private PreferentialLogBiz preferentialLogBiz;

    @Autowired
    private CustomerBiz customerBiz;
    @Autowired
    private CommunicateRecordBiz communicateRecordBiz;
    @Autowired
    private InsuranceBiz insuranceBiz;


    @RequestMapping("home")
    public ModelAndView home() {
        ModelAndView view = new ModelAndView("mana/view/customer/customerHome");

        return view;
    }

    @RequestMapping("addPage")
    public ModelAndView addCustomerPage(Integer customerId) {
        ModelAndView view = new ModelAndView("mana/view/customer/addCustomer");
        Result<ManaCustomerDO> result = customerBiz.getCustomerById(customerId);
        if (result.isSuccess()) {
            view.addObject("customerInfo", result.getData());
        }

        return view;
    }

    @RequestMapping("listPage")
    public ModelAndView listPage() {
        ModelAndView view = new ModelAndView("mana/view/customer/infoList");
        view.addObject("originalArtTemplate", 1);
        view.addObject("allCompanyList", insuranceBiz.getAllCompanyList());

        return view;
    }

    /**
     * 客户详细信息页面
     *
     * @return
     */
    @RequestMapping("detailPage")
    public ModelAndView detailPage(Integer vehicleId) {

        ModelAndView view = new ModelAndView("mana/view/customer/customerDetail");
        view.addObject("vehicleId", vehicleId);
        //view.addObject("originalArtTemplate", 1);
        Result<SearchCustomerBO> result = customerBiz.searchCustomerDetailById(vehicleId);
        if (result.isSuccess()) {
            SearchCustomerBO customer = result.getData();
            view.addObject("customer", customer);
        }
        return view;
    }

    /**
     * 新增客户
     *
     * @param addCustomerBO
     * @return
     */
    @RequestMapping(value = "addCustomer", method = RequestMethod.POST)
    @ResponseBody
    public Result addCustomer(AddCustomerBO addCustomerBO) {
        return customerBiz.addCustomer(addCustomerBO);
    }

    /**
     * 新增车辆
     *
     * @param addCustomerBO
     * @return
     */
    @RequestMapping(value = "addCustomerVehicle", method = RequestMethod.POST)
    @ResponseBody
    public Result addCustomerVehicle(AddCustomerBO addCustomerBO) {
        return customerBiz.addCustomerVehicle(addCustomerBO);
    }

    /**
     * 新增沟通记录
     *
     * @param communicateRecordBO
     * @return
     */
    @RequestMapping(value = "addCommunicateRecord", method = RequestMethod.POST)
    @ResponseBody
    public Result addCommunicateRecord(AddCommunicateRecordBO communicateRecordBO) {
        return communicateRecordBiz.addCommunicateRecord(communicateRecordBO);
    }

    /**
     * 新增优惠记录列表
     *
     * @param addPreferentialVO
     * @return
     */
    @RequestMapping(value = "addPreferentialList", method = RequestMethod.POST)
    @ResponseBody
    public Result addPreferentialList(AddPreferentialVO addPreferentialVO) {

        return preferentialLogBiz.addPreferentialLogList(addPreferentialVO);

    }

    /**
     * 新增客户优惠记录
     *
     * @param addPreferentialLogBO
     * @return
     */
    @RequestMapping(value = "addPreferentialLog", method = RequestMethod.POST)
    @ResponseBody
    public Result addPreferentialLog(AddPreferentialLogBO addPreferentialLogBO) {

        return preferentialLogBiz.addPreferentialLog(addPreferentialLogBO);
    }

    /**
     * 客户来电查询
     *
     * @param queryStr
     * @return
     */
    @RequestMapping("searchCustomer")
    @ResponseBody
    public PagingResult<CustomerVehiclePageBO> searchCustomer(String queryStr, Integer pageSize, Integer pageNumber) {
        return customerBiz.searchCustomer(queryStr, pageSize, pageNumber);
    }

    /**
     * 通过车辆id获取车主详细信息
     *
     * @param vehicleId
     * @return
     */
    @RequestMapping("searchCustomerDetailById")
    @ResponseBody
    public Result<SearchCustomerBO> searchCustomerDetailById(Integer vehicleId) {
        return customerBiz.searchCustomerDetailById(vehicleId);
    }

    /**
     * 客户业务列表查询
     *
     * @param commonVOPageParam
     * @return
     */
    @RequestMapping("getCustomerFormPagingResult")
    @ResponseBody
    public PagingResult<CustomerPageBO> searchCustomerFormPagingResult(CommonVOPageParam commonVOPageParam) {
        return customerBiz.searchCustomerFormPagingResult(commonVOPageParam);
    }

    /**
     * 分页获取车辆沟通记录信息
     *
     * @param commonVOPageParam
     * @return
     */
    @RequestMapping("getCommunicationRecord")
    @ResponseBody
    public PagingResult<SearchCommunicateRecordBO> searchCommunicateRecordPagingResult(CommonVOPageParam commonVOPageParam) {
        return communicateRecordBiz.searchCommunicationRecordPagingResult(commonVOPageParam);
    }

    /**
     * 更新客户信息
     * @param updateCustomerInfoBO
     * @return
     */
    @RequestMapping(value = "updateCustomerAndVehicle",method = RequestMethod.POST)
    @ResponseBody
    public Result updateCustomerAndVehicle(UpdateCustomerInfoBO updateCustomerInfoBO){
        return customerBiz.updateCustomerAndVehicle(updateCustomerInfoBO);
    }

    /**
     * 修改客户地址
     * @param addressBO
     * @return
     */
    @RequestMapping(value = "modifyAddress", method = RequestMethod.POST)
    @ResponseBody
    public Result modifyAddress(UpdateCustomerAddressBO addressBO){
        return customerBiz.modifyAddress(addressBO);
    }

    /**
     * 查询客户基本信息
     * @param id
     * @return
     */
    @RequestMapping("getCustomerInfoById")
    @ResponseBody
    public Result<CustomerInfoBO> getCustomerInfoById(Integer id){
        return customerBiz.getCustomerInfoById(id);
    }

    /**
     * 获取客户地址
     * @param customerId
     * @return
     */
    @RequestMapping("getAddressByCustomerId")
    @ResponseBody
    public Result<CustomerAddressBO> getAddressByCustomerId(Integer customerId){
        return customerBiz.getAddressByCustomerId(customerId);
    }

}
