package com.tqmall.mana.web.controller.settle;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.InsuranceRegionDTO;
import com.tqmall.mana.beans.VO.settle.shopRule.ModifyShopRuleVO;
import com.tqmall.mana.beans.VO.settle.shopRule.SettleShopRuleVO;
import com.tqmall.mana.beans.VO.settle.shopRule.add.AddSettleShopRuleVO;
import com.tqmall.mana.beans.param.settle.SettleShopRulePageParam;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.beans.param.settle.SettleShopRuleRegionParam;
import com.tqmall.mana.biz.manager.settle.config.SettleInsuranceCompanyRuleBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleShopRuleBiz;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by huangzhangting on 17/1/17.
 */
@Controller
@RequestMapping("settle/shopRule")
public class SettleShopRuleController extends BaseController {
    @Autowired
    private SettleShopRuleBiz settleShopRuleBiz;
    @Autowired
    private SettleInsuranceCompanyRuleBiz settleInsuranceCompanyRuleBiz;
    @Autowired
    private InsuranceBiz insuranceBiz;


    @RequestMapping("bountyScaleList")
    public ModelAndView bountyScaleList() {
        ModelAndView view = new ModelAndView("mana/view/settle/bountyScaleList");

        return view;
    }

    //新增页面
    @RequestMapping("bountyScaleManage")
    public ModelAndView bountyScaleManage() {
        ModelAndView view = new ModelAndView("mana/view/settle/bountyScaleManage");
        view.addObject("allCompanyList", insuranceBiz.getAllCompanyList());
        Result<List<InsuranceRegionDTO>> result = settleInsuranceCompanyRuleBiz.getRegionList(null);
        if (result.isSuccess()) {
            view.addObject("regionList", result.getData());
        }
        return view;
    }

    //编辑页面
    @RequestMapping("bountyScaleManageEdit")
    public ModelAndView bountyScaleManageEdit(Integer bountyScaleId) {
        ModelAndView view = new ModelAndView("mana/view/settle/bountyScaleManageEdit");
        view.addObject("bountyScaleId",bountyScaleId);
        view.addObject("shopRuleBO",settleShopRuleBiz.getSettleShopRule(bountyScaleId));
        view.addObject("allCompanyList", insuranceBiz.getAllCompanyList());
        Result<List<InsuranceRegionDTO>> result = settleInsuranceCompanyRuleBiz.getRegionList(null);
        if (result.isSuccess()) {
            view.addObject("regionList", result.getData());
        }
        return view;
    }

    @RequestMapping("servicePackageList")
    public ModelAndView servicePackageList() {
        ModelAndView view = new ModelAndView("mana/view/settle/servicePackageList");

        return view;
    }

    @RequestMapping("servicePackageManage")
    public ModelAndView servicePackageManage() {
        ModelAndView view = new ModelAndView("mana/view/settle/servicePackageManage");

        return view;
    }


    /**
     * 分页查询规则列表
     */
    @RequestMapping("queryShopRulePage")
    @ResponseBody
    public PagingResult<SettleShopRuleVO> queryShopRulePage(SettleShopRulePageParam param) {
        return settleShopRuleBiz.queryShopRulePage(param);
    }


    /**
     * 删除规则
     */
    @RequestMapping("deleteShopRule")
    @ResponseBody
    public Result deleteShopRule(Integer id) {
        return ResultUtil.successResult(settleShopRuleBiz.deleteShopRule(id));
    }


    /**
     * 修改规则，服务包模式
     */
    @RequestMapping(value = "modifyShopRule", method = RequestMethod.POST)
    @ResponseBody
    public Result modifyShopRule(@RequestBody ModifyShopRuleVO modifyShopRuleVO){
        return ResultUtil.successResult(settleShopRuleBiz.modifyShopRule(modifyShopRuleVO));
    }

    /**
     * 修改规则，奖励金模式
     */
    @RequestMapping(value = "modifyShopRuleForReward", method = RequestMethod.POST)
    @ResponseBody
    public Result modifyShopRuleForReward(@RequestBody ModifyShopRuleVO modifyShopRuleVO) {
        return ResultUtil.successResult(settleShopRuleBiz.modifyShopRuleForReward(modifyShopRuleVO));
    }


    /**
     * 添加规则，服务包模式
     */
    @RequestMapping(value = "addShopRule", method = RequestMethod.POST)
    @ResponseBody
    public Result addShopRule(@RequestBody AddSettleShopRuleVO shopRuleVO){
        return ResultUtil.successResult(settleShopRuleBiz.addSettleShopRule(shopRuleVO));
    }

    /**
     * 添加规则，奖励金模式
     */
    @RequestMapping(value = "addShopRuleForReward", method = RequestMethod.POST)
    @ResponseBody
    public Result addShopRuleForReward(@RequestBody AddSettleShopRuleVO shopRuleVO) {
        return ResultUtil.successResult(settleShopRuleBiz.addSettleShopRuleForReward(shopRuleVO));
    }

    /**
     * 根据主键id查询
     */
    @RequestMapping("getShopRuleById")
    @ResponseBody
    public Result getShopRuleById(Integer id) {
        return ResultUtil.successResult(settleShopRuleBiz.getSettleShopRule(id));
    }

    @RequestMapping("getAvailableRegionList")
    @ResponseBody
    public Result<List<InsuranceRegionDTO>> getAvailableRegionList(SettleShopRuleRegionParam settleShopRuleRegionParam) {
        return settleInsuranceCompanyRuleBiz.getAvailableRegionList(settleShopRuleRegionParam);
    }

}
