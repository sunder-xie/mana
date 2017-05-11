package com.tqmall.mana.web.controller.belazy;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.VO.settle.AddMaterialAllowanceVO;
import com.tqmall.mana.beans.VO.settle.SettleFeeModifyVO;
import com.tqmall.mana.beans.VO.settle.UseCashCouponVO;
import com.tqmall.mana.beans.param.settle.SettleFeeModifyPO;
import com.tqmall.mana.beans.param.settle.UseCashCouponPO;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.biz.manager.insurance.InsuranceSettleSyncBiz;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopCheckBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleFormulaConfigBiz;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by huangzhangting on 17/3/16.
 */
@Slf4j
@Controller
@RequestMapping("insuranceDataSync")
public class InsuranceDataSyncController extends BaseController{
    @Autowired
    private InsuranceSettleSyncBiz insuranceSettleSyncBiz;
    @Autowired
    private SettleShopCheckBiz settleShopCheckBiz;
    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private InsuranceBiz insuranceBiz;
    @Autowired
    private SettleFormulaConfigBiz formulaConfigBiz;


    @RequestMapping("index")
    public String index(){
        return "mana/view/belazy/dataSync";
    }

    /**
     * 根据车牌号同步结算数据
     * @param vehicleSn
     * @return
     */
    @RequestMapping("syncSettleDataByVehicleSn")
    @ResponseBody
    public Result syncSettleDataByVehicleSn(String vehicleSn){
        String operator = shiroBiz.getCurrentUserRealName();
        log.info("syncSettleDataByVehicleSn, operator:{}, param:{}", operator, vehicleSn);
        insuranceSettleSyncBiz.syncDataByVehicleSn(vehicleSn);
        return ResultUtil.successResult("", "结算数据同步成功");
    }


    @RequestMapping("useCashCouponPage")
    public ModelAndView useCashCouponPage(){
        ModelAndView view = new ModelAndView("mana/view/belazy/useCashCouponPage");
        view.addObject("insuranceCompanyList", insuranceBiz.getAllCompanyList());
        view.addObject("formulaKeyList", formulaConfigBiz.getAllFormulaKeys());
        return view;
    }
    /**
     * 使用现金券
     * （如果insurance那边使用现金券，修改结算数据失败了，可以使用这个接口，手动修改结算数据）
     * （只是以防万一，写sql改数据，那就操蛋了）
     */
    @RequestMapping(value = "useCashCoupon", method = RequestMethod.POST)
    @ResponseBody
    public Result useCashCoupon(UseCashCouponVO useCashCouponVO){
        String operator = shiroBiz.getCurrentUserRealName();
        log.info("useCashCoupon, operator:{}, param:{}", operator, JsonUtil.objectToStr(useCashCouponVO));
        UseCashCouponPO useCashCouponPO = BdUtil.do2bo(useCashCouponVO, UseCashCouponPO.class);
        return ResultUtil.successResult(settleShopCheckBiz.useCashCoupon(useCashCouponPO));
    }


    /**
     * 撤销使用现金券
     */
    @RequestMapping("unUseCashCouponPage")
    public String unUseCashCouponPage(){
        return "mana/view/belazy/unUseCashCouponPage";
    }
    @RequestMapping(value = "unUseCashCoupon", method = RequestMethod.POST)
    @ResponseBody
    public Result unUseCashCoupon(SettleFeeModifyVO modifyVO){
        String operator = shiroBiz.getCurrentUserRealName();
        log.info("unUseCashCoupon, operator:{}, param:{}", operator, JsonUtil.objectToStr(modifyVO));
        SettleFeeModifyPO modifyPO = BdUtil.do2bo(modifyVO, SettleFeeModifyPO.class);
        return ResultUtil.successResult(settleShopCheckBiz.unUseCashCoupon(modifyPO));
    }


    /**
     * 补充机滤补贴
     */
    @RequestMapping("addMaterialAllowancePage")
    public String addMaterialAllowancePage(){
        return "mana/view/belazy/addMaterialAllowancePage";
    }
    @RequestMapping(value = "addMaterialAllowance", method = RequestMethod.POST)
    @ResponseBody
    public Result addMaterialAllowance(AddMaterialAllowanceVO allowanceVO){
        String operator = shiroBiz.getCurrentUserRealName();
        log.info("addMaterialAllowance, operator:{}, param:{}", operator, JsonUtil.objectToStr(allowanceVO));
        insuranceSettleSyncBiz.addMaterialAllowance(allowanceVO);
        return ResultUtil.successResult("添加机滤补贴成功");
    }

}
