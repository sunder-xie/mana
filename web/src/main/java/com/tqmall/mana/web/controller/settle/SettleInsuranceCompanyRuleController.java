package com.tqmall.mana.web.controller.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleCompanyRulePageBO;
import com.tqmall.mana.beans.VO.settle.SettleCompanyRuleSearchVO;
import com.tqmall.mana.beans.VO.settle.SettleInsuranceCompanyRuleVO;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleInsuranceCompanyRuleBiz;
import com.tqmall.mana.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jinju.zeng on 17/1/14.
 */
@Controller
@RequestMapping("settle/insuranceCompanyRule")
@Slf4j
public class SettleInsuranceCompanyRuleController extends BaseController {

    @Autowired
    private SettleInsuranceCompanyRuleBiz settleInsuranceCompanyRuleBiz;
    @Autowired
    private InsuranceBiz insuranceBiz;


    @RequestMapping("rulePage")
    public ModelAndView insuranceCompanyRulePage(Integer id) {
        ModelAndView view = new ModelAndView("mana/view/settle/insuranceCompanyRulePage");
        view.addObject("allCompanyList", insuranceBiz.getAllCompanyList());
        view.addObject("id", id);
        return view;
    }

    @RequestMapping("listPage")
    public ModelAndView insuranceCompanyRuleListPage() {
        ModelAndView view = new ModelAndView("mana/view/settle/insuranceCompanyRuleList");
        return view;
    }

    @RequestMapping("list")
    @ResponseBody
    public PagingResult<SettleCompanyRulePageBO> getCompanyRuleList(SettleCompanyRuleSearchVO searchVO) {
        return settleInsuranceCompanyRuleBiz.searchInsuranceCompanyRuleForPage(searchVO);
    }

    @RequestMapping("regionListForSelect")
    @ResponseBody
    public Result getRegionList(@RequestParam("regionParentCode") String regionParentCode) {
        return settleInsuranceCompanyRuleBiz.getRegionList(regionParentCode);
    }

    @RequestMapping("regionList")
    @ResponseBody
    public Result getRegionListForSelect(@RequestParam("regionCode") String regionParentCode) {
        return settleInsuranceCompanyRuleBiz.getInsuredRegionAndIsOpen(regionParentCode);
    }

    @RequestMapping(value = "saveRule", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Result saveRule(@RequestBody SettleInsuranceCompanyRuleVO settleInsuranceCompanyRuleVO) {
        return Result.wrapSuccessfulResult(settleInsuranceCompanyRuleBiz.saveInsuranceCompanyRule(settleInsuranceCompanyRuleVO));
    }

    @RequestMapping("delete")
    @ResponseBody
    public Result deleteRuleById(@RequestParam("id") Integer id) {
        Boolean result = settleInsuranceCompanyRuleBiz.deleteInsuranceCompanyRuleById(id);
        if (!result) {
            return Result.wrapErrorResult("", "删除失败");
        }
        return Result.wrapSuccessfulResult("删除成功");
    }

    @RequestMapping("getById")
    @ResponseBody
    public Result getById(Integer id) {
        return settleInsuranceCompanyRuleBiz.getInsuranceCompanyRuleById(id);
    }


}
