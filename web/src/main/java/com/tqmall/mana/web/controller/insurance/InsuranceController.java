package com.tqmall.mana.web.controller.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.InsuranceRegionDTO;
import com.tqmall.mana.beans.BO.insurance.InsuranceCompanyBO;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceFormBO;
import com.tqmall.mana.beans.param.CommonVOPageParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleInsuranceCompanyRuleBiz;
import com.tqmall.mana.component.bean.DataError;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zhouheng on 16/12/20.
 */
@RequestMapping("insurance")
@Controller
@Slf4j
public class InsuranceController extends BaseController{

    @Autowired
    private InsuranceBiz insuranceBiz;

    @Autowired
    private SettleInsuranceCompanyRuleBiz settleInsuranceCompanyRuleBiz;


    /**
     * 保单详情页面
     * @return
     */
    @RequestMapping("insureDetail")
    public ModelAndView insureDetailPage(Integer formId,Integer agentId){
        ModelAndView view = new ModelAndView("mana/view/insurance/insureDetail");

        view.addObject("formId",formId);
        view.addObject("agentId",agentId);

        Result<InsuranceFormBO> result = insuranceBiz.getInsuranceFormInfo(formId,agentId);
        if(result.isSuccess()){
            view.addObject("insurance",result.getData());
        }
        return view;
    }
    /**
     * 通过车辆ID获取车辆下历史保单信息列表
     * @param commonVOPageParam
     * @return
     */
    @ResponseBody
    @RequestMapping("getInsuranceFormPagingResult")
    public Result<List<InsuranceFormBO>> getInsuranceFormInfo(CommonVOPageParam commonVOPageParam){

        return insuranceBiz.getInsuranceFormPagingResult(commonVOPageParam);

    }

    /**
     * 通过保单id和代理人id获取保单详情信息
     * @param formId
     * @param agentId
     * @return
     */
    @RequestMapping("getInsuranceFormInfo")
    @ResponseBody
    public Result<InsuranceFormBO> getInsuranceFormInfo(Integer formId,Integer agentId){

        return insuranceBiz.getInsuranceFormInfo(formId,agentId);

    }

    /**
     * 获取所有保险公司列表
     * @return
     */
    @RequestMapping("getAllCompanyList")
    @ResponseBody
    public Result<List<InsuranceCompanyBO>> getAllCompanyList(){

        List<InsuranceCompanyBO> companyBOList = insuranceBiz.getAllCompanyList();
        if(!CollectionUtils.isEmpty(companyBOList)){
            return Result.wrapSuccessfulResult(companyBOList);
        }
        return ResultUtil.errorResult(DataError.NO_DATA_ERROR);
    }


    /**
     * 获取保险城市站下拉列表
     *
     * @param regionParentCode 000000：查询中国省份
     * @return
     */
    @RequestMapping("getRegionList")
    @ResponseBody
    public Result<List<InsuranceRegionDTO>> getRegionList(String regionParentCode){
        return settleInsuranceCompanyRuleBiz.getRegionList(regionParentCode);
    }

}
