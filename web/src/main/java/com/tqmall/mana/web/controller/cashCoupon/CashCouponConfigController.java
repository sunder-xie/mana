package com.tqmall.mana.web.controller.cashCoupon;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRegionConfigBO;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRuleConfigBO;
import com.tqmall.mana.beans.param.settle.CashCouponConfigSearchParam;
import com.tqmall.mana.beans.param.settle.CashCouponRuleConfigPO;
import com.tqmall.mana.biz.manager.coupon.CashCouponRuleConfigBiz;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.web.common.BaseController;
import com.tqmall.tqmallstall.domain.result.RegionListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zhouheng on 17/4/10.
 */
@RequestMapping("cashCouponConfig")
@Controller
public class CashCouponConfigController extends BaseController {
    @Autowired
    private CashCouponRuleConfigBiz cashCouponRuleConfigBiz;


    @RequestMapping("addConfigInit")
    public ModelAndView addConfigInit() {

        ModelAndView modelAndView = new ModelAndView("mana/view/settle/cashCoupon/cashCouponRuleAdd");

        return modelAndView;

    }

    /**
     * 跳转到规则列表页面
     *
     * @return
     */
    @RequestMapping("config/list")
    public ModelAndView configList() {

        ModelAndView modelAndView = new ModelAndView("mana/view/settle/cashCoupon/cashCouponRuleList");

        return modelAndView;

    }

    /**
     * 编辑
     *
     * @param regionConfigId
     * @return
     */
    @RequestMapping("getConfigInfoById")
    @ResponseBody
    public Result<CashCouponRuleConfigPO> getConfigInfoById(Integer regionConfigId) {

        return Result.wrapSuccessfulResult(cashCouponRuleConfigBiz.getConfigInfoById(regionConfigId));

    }

    /**
     * 删除
     *
     * @param regionConfigId
     * @return
     */
    @RequestMapping("deleteConfigInfo")
    @ResponseBody
    public Result<Void> deleteConfigInfo(Integer regionConfigId) {

        cashCouponRuleConfigBiz.deleteConfigInfo(regionConfigId);

        return Result.wrapSuccessfulResult(null);
    }


    /**
     * 新增或者编辑现金券接口
     *
     * @param cashCouponRuleConfigPO
     * @return
     */
    @RequestMapping(value = "addConfig",method = RequestMethod.POST)
    @ResponseBody
    public Result addConfig(@RequestBody CashCouponRuleConfigPO cashCouponRuleConfigPO) {
        boolean flag;
        if (cashCouponRuleConfigPO == null) {
            Result.wrapErrorResult("参数不能为空", "01111");
        }
        if (cashCouponRuleConfigPO.getId() != null) {
            flag = cashCouponRuleConfigBiz.updateCashCouponRuleConfig(cashCouponRuleConfigPO);
        } else {
            flag = cashCouponRuleConfigBiz.createCashCouponRuleConfig(cashCouponRuleConfigPO);
        }
        return ResultUtil.successResult(flag);

    }


    @RequestMapping("getOpenedRegionList")
    @ResponseBody
    public Result<List<RegionListDTO>> getAllOpenedRegionList() {

        return cashCouponRuleConfigBiz.getAllOpenedRegionList();

    }

    /**
     * 规则列表数据分页获取
     *
     * @param searchParam
     * @return
     */
    @RequestMapping("getConfigInfo")
    @ResponseBody
    public PagingResult<CashCouponRegionConfigBO> getRegionConfigPageList(CashCouponConfigSearchParam searchParam) {

        return cashCouponRuleConfigBiz.getRegionConfigPageList(searchParam);

    }

}
