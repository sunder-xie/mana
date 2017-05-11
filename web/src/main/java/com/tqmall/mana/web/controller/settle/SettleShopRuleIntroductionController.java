package com.tqmall.mana.web.controller.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.SettleShopRuleIntroductionBO;
import com.tqmall.mana.biz.manager.settle.config.SettleShopRuleIntroductionBiz;
import com.tqmall.mana.web.common.BaseController;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zhouheng on 17/2/7.
 */
@Data
@Controller
@RequestMapping("introduction")
public class SettleShopRuleIntroductionController extends BaseController {

    @Autowired
    private SettleShopRuleIntroductionBiz introductionBiz;

    /**
     * 列表页
     *
     * @return
     */
    @RequestMapping("shopRuleIntroductionInit")
    public ModelAndView shopRuleIntroductionInit() {

        ModelAndView modelAndView = new ModelAndView("mana/view/settle/shopRuleIntroductionList");
        Result<List<SettleShopRuleIntroductionBO>> result = introductionBiz.getRuleIntroductionList();
        if (result.isSuccess()) {
            modelAndView.addObject("introductionBOList", result.getData());
        }
        return modelAndView;
    }

    /**
     * 编辑页
     *
     * @param ruleType
     * @return
     */
    @RequestMapping("shopRuleIntroductionEdit")
    public ModelAndView shopRuleIntroductionEdit(Integer ruleType) {
        ModelAndView modelAndView = new ModelAndView("mana/view/settle/shopRuleIntroductionEdit");
        Result<SettleShopRuleIntroductionBO> result = introductionBiz.getRuleIntroductionByRuleType(ruleType);
        if(result.isSuccess() && result.getData() != null){
            SettleShopRuleIntroductionBO introductionBO = result.getData();
            modelAndView.addObject("introductionId",introductionBO.getId());
        }
        modelAndView.addObject("ruleType",ruleType);
        return modelAndView;
    }

    /**
     * 通过规则说明id获取规则说明信息
     *
     * @param introductionId
     * @return
     */
    @ResponseBody
    @RequestMapping("getRuleIntroductionInfo")
    public Result<SettleShopRuleIntroductionBO> getRuleIntroductionInfo(Integer introductionId) {

        return introductionBiz.getRuleIntroductionInfo(introductionId);

    }

    @ResponseBody
    @RequestMapping("getRuleIntroductionByRuleType")
    public Result<SettleShopRuleIntroductionBO> getRuleIntroductionByRuleType(Integer ruleType){

        return introductionBiz.getRuleIntroductionByRuleType(ruleType);

    }

    /**
     * 获取所有规则说明列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getRuleIntroductionList")
    public Result<List<SettleShopRuleIntroductionBO>> getRuleIntroductionList() {

        return introductionBiz.getRuleIntroductionList();

    }

    /**
     * 新增规则说明
     *
     * @param introductionBO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addRuleIntroduction", method = RequestMethod.POST)
    public Result addRuleIntroduction(SettleShopRuleIntroductionBO introductionBO) {

        return introductionBiz.addRuleIntroduction(introductionBO);

    }

    /**
     * 更新规则说明
     *
     * @param introductionBO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateIntroduction", method = RequestMethod.POST)
    public Result updateIntroduction(SettleShopRuleIntroductionBO introductionBO) {

        return introductionBiz.updateIntroduction(introductionBO);

    }

}
