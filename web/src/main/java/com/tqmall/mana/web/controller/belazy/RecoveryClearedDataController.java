package com.tqmall.mana.web.controller.belazy;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.entity.settle.*;
import com.tqmall.mana.biz.manager.shiro.ShiroBiz;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.dao.mapper.settle.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 *
 * 已清理的数据恢复
 *
 * Created by huangzhangting on 17/3/13.
 */
@Slf4j
@Controller
@RequestMapping("recoveryClearedData")
public class RecoveryClearedDataController {
    private static final String NOT_DELETE = "N";

    @Autowired
    private ShiroBiz shiroBiz;
    @Autowired
    private SettleShopCheckDetailDOMapper shopCheckDetailDOMapper;
    @Autowired
    private SettleFormDOMapper settleFormDOMapper;
    @Autowired
    private SettleServiceCheckDetailDOMapper serviceCheckDetailDOMapper;
    @Autowired
    private SettleCarOwnerCheckDetailDOMapper carOwnerCheckDetailDOMapper;
    @Autowired
    private SettleCompanyCheckDetailDOMapper companyCheckDetailDOMapper;


    @RequestMapping("recoverySettleDataByInsuranceOrderSn")
    @ResponseBody
    public Result recoverySettleDataByInsuranceOrderSn(String insuranceOrderSn){
        if(StringUtils.isEmpty(insuranceOrderSn)){
            return ResultUtil.errorResult("", "淘汽保单号不能为空");
        }
        String logKey = "to recovery shop settle data.";
        String operator = shiroBiz.getCurrentUserRealName();
        log.info("{} operator:{}, insuranceOrderSn:{}", logKey, operator, insuranceOrderSn);

        Date now = new Date();
        List<String> insuranceOrderSnList = new ArrayList<>();
        insuranceOrderSnList.add(insuranceOrderSn);

        /* 恢复 settle_shop_check_detail */
        List<SettleShopCheckDetailDO> shopCheckDetailDOList = shopCheckDetailDOMapper.getDeletedData(insuranceOrderSnList);
        for(SettleShopCheckDetailDO shopCheckDetailDO : shopCheckDetailDOList){
            SettleShopCheckDetailDO updateDO = new SettleShopCheckDetailDO();
            updateDO.setId(shopCheckDetailDO.getId());
            updateDO.setGmtModified(now);
            updateDO.setModifier(operator);
            updateDO.setIsDeleted(NOT_DELETE);
            shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
        }

        /* 恢复 settle_form */
        Map<Integer, List<String>> formSnMap = new HashMap<>();
        List<SettleFormDO> settleFormDOList = settleFormDOMapper.getDeletedData(insuranceOrderSnList);
        for(SettleFormDO formDO : settleFormDOList){
            SettleFormDO updateDO = new SettleFormDO();
            updateDO.setId(formDO.getId());
            updateDO.setGmtModified(now);
            updateDO.setModifier(operator);
            updateDO.setIsDeleted(NOT_DELETE);
            settleFormDOMapper.updateByPrimaryKeySelective(updateDO);

            Integer insuranceCompanyId = formDO.getInsuranceCompanyId();
            List<String> formSnList = formSnMap.get(insuranceCompanyId);
            if(formSnList==null){
                formSnList = new ArrayList<>();
                formSnMap.put(insuranceCompanyId, formSnList);
            }
            formSnList.add(formDO.getInsuredFormNo());
        }

        recoverySettleDataCommon(insuranceOrderSnList, formSnMap, now, operator);

        String msg = "insuranceOrderSn="+insuranceOrderSn
                +", formSnMap="+formSnMap.toString();

        log.info("{} {}", logKey, msg);
        return ResultUtil.successResult("数据恢复成功, "+msg);
    }


    private void recoverySettleDataCommon(List<String> insuranceOrderSnList, Map<Integer, List<String>> formSnMap,
                                          Date now, String operator){
        if(!insuranceOrderSnList.isEmpty()) {
            /* 恢复 settle_service_check_detail */
            List<SettleServiceCheckDetailDO> serviceCheckDetailDOList = serviceCheckDetailDOMapper.getDeletedData(insuranceOrderSnList);
            for (SettleServiceCheckDetailDO serviceCheckDetailDO : serviceCheckDetailDOList) {
                SettleServiceCheckDetailDO updateDO = new SettleServiceCheckDetailDO();
                updateDO.setId(serviceCheckDetailDO.getId());
                updateDO.setGmtModified(now);
                updateDO.setModifier(operator);
                updateDO.setIsDeleted(NOT_DELETE);
                serviceCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
            }

            /* 恢复 settle_car_owner_check_detail */
            List<SettleCarOwnerCheckDetailDO> carOwnerCheckDetailDOList = carOwnerCheckDetailDOMapper.getDeletedData(insuranceOrderSnList);
            for (SettleCarOwnerCheckDetailDO carOwnerCheckDetailDO : carOwnerCheckDetailDOList) {
                SettleCarOwnerCheckDetailDO updateDO = new SettleCarOwnerCheckDetailDO();
                updateDO.setId(carOwnerCheckDetailDO.getId());
                updateDO.setGmtModified(now);
                updateDO.setModifier(operator);
                updateDO.setIsDeleted(NOT_DELETE);
                carOwnerCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
            }
        }

        if(!formSnMap.isEmpty()){
            /* 恢复 settle_company_check_detail */
            for(Map.Entry<Integer, List<String>> entry : formSnMap.entrySet()) {
                List<SettleCompanyCheckDetailDO> companyCheckDetailDOList =
                        companyCheckDetailDOMapper.getDeletedData(entry.getValue(), entry.getKey());
                for (SettleCompanyCheckDetailDO companyCheckDetailDO : companyCheckDetailDOList) {
                    SettleCompanyCheckDetailDO updateDO = new SettleCompanyCheckDetailDO();
                    updateDO.setId(companyCheckDetailDO.getId());
                    updateDO.setGmtModified(now);
                    updateDO.setModifier(operator);
                    updateDO.setIsDeleted(NOT_DELETE);
                    companyCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
                }
            }
        }
    }
}
