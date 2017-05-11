package com.tqmall.mana.web.controller;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.VO.InsurancePriceParityVO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceItemDO;
import com.tqmall.mana.biz.manager.insurance.InsuranceBiz;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保险比价
 * Created by huangzhangting on 16/12/1.
 */
@Controller
@RequestMapping("priceParity")
public class PriceParityController extends BaseController{
    @Autowired
    private InsuranceBiz insuranceBiz;


    @RequestMapping("")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("mana/view/priceParity");

        return view;
    }

    @RequestMapping("getVOList")
    @ResponseBody
    public Result<List<InsurancePriceParityVO>> getVOList(String vehicleSn){
        return insuranceBiz.getInsurancePriceParityVOList(vehicleSn);
    }

    @RequestMapping("getItemList")
    @ResponseBody
    public Result getItemList(Integer insuranceBasicId){
        Result<List<ManaInsuranceItemDO>> result = insuranceBiz.getInsuranceItemDOList(insuranceBasicId);
        if(!result.isSuccess()){
            return result;
        }

        List<ManaInsuranceItemDO> list = result.getData();
        List<ManaInsuranceItemDO> vciItemList = new ArrayList<>(); //商业险项目
        List<ManaInsuranceItemDO> tciItemList = new ArrayList<>(); //交强险项目

        //保费合计
        BigDecimal feeAmountAnXin = BigDecimal.ZERO;
        BigDecimal feeAmountRenBao = BigDecimal.ZERO;
        BigDecimal feeAmountPingAn = BigDecimal.ZERO;

        for(ManaInsuranceItemDO itemDO : list){
            switch (itemDO.getInsuranceType()){
                case 1: tciItemList.add(itemDO); break;
                case 2: vciItemList.add(itemDO); break;
                default: break;
            }

            feeAmountAnXin = feeAmountAnXin.add(itemDO.getInsuranceFee());
            feeAmountRenBao = feeAmountRenBao.add(itemDO.getInsuranceFeeRenBao());
            feeAmountPingAn = feeAmountPingAn.add(itemDO.getInsuranceFeePingAn());

        }

        Map<String, Object> map = new HashMap<>();
        map.put("feeAmountAnXin", feeAmountAnXin);
        map.put("feeAmountRenBao", feeAmountRenBao);
        map.put("feeAmountPingAn", feeAmountPingAn);
        map.put("vciItemSize", vciItemList.size());
        map.put("vciItemList", vciItemList);
        map.put("tciItemSize", tciItemList.size());
        map.put("tciItemList", tciItemList);

        return ResultUtil.successResult(map);
    }

}
