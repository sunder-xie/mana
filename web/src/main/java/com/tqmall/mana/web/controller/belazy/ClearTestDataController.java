package com.tqmall.mana.web.controller.belazy;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.entity.settle.*;
import com.tqmall.mana.beans.param.settle.SettleShopCheckDetailQueryPO;
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
 * 测试数据清理
 *
 * Created by huangzhangting on 17/3/13.
 */
@Slf4j
@Controller
@RequestMapping("clearTestData")
public class ClearTestDataController {
    private static final String TEST_CLEANER = "test-cleaner";
    private static final String HAS_DELETED = "Y";

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


    @RequestMapping("clearShopSettleData")
    @ResponseBody
    public Result clearShopSettleData(Integer shopId){
        if(shopId==null){
            return ResultUtil.errorResult("", "门店id，不能为空");
        }
        String logKey = "to clear shop settle data.";
        String operator = shiroBiz.getCurrentUserRealName();
        log.info("{} operator:{}, shopId:{}", logKey, operator, shopId);

        Date now = new Date();
        Set<String> insuranceOrderSns = new HashSet<>();

        /* 清理 settle_shop_check_detail */
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setAgentId(shopId);
        List<SettleShopCheckDetailDO> shopCheckDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        for(SettleShopCheckDetailDO shopCheckDetailDO : shopCheckDetailDOList){
            SettleShopCheckDetailDO updateDO = new SettleShopCheckDetailDO();
            updateDO.setId(shopCheckDetailDO.getId());
            updateDO.setGmtModified(now);
            updateDO.setModifier(TEST_CLEANER);
            updateDO.setIsDeleted(HAS_DELETED);
            shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);

            insuranceOrderSns.add(shopCheckDetailDO.getInsuranceOrderSn());
        }

        /* 清理 settle_form */
        Map<Integer, List<String>> formSnMap = new HashMap<>();
        List<SettleFormDO> settleFormDOList = settleFormDOMapper.selectByAgentId(shopId);
        for(SettleFormDO formDO : settleFormDOList){
            SettleFormDO updateDO = new SettleFormDO();
            updateDO.setId(formDO.getId());
            updateDO.setGmtModified(now);
            updateDO.setModifier(TEST_CLEANER);
            updateDO.setIsDeleted(HAS_DELETED);
            settleFormDOMapper.updateByPrimaryKeySelective(updateDO);

            insuranceOrderSns.add(formDO.getInsuranceOrderSn()); //这个处理实际上没什么意义

            Integer insuranceCompanyId = formDO.getInsuranceCompanyId();
            List<String> formSnList = formSnMap.get(insuranceCompanyId);
            if(formSnList==null){
                formSnList = new ArrayList<>();
                formSnMap.put(insuranceCompanyId, formSnList);
            }
            formSnList.add(formDO.getInsuredFormNo());
        }

        List<String> insuranceOrderSnList = new ArrayList<>(insuranceOrderSns);

        clearSettleDataCommon(insuranceOrderSnList, formSnMap, now);

        String msg = "shopId="+shopId
                +", insuranceOrderSnList="+insuranceOrderSnList.toString()
                +", formSnMap="+formSnMap.toString();

        log.info("{} {}", logKey, msg);
        return ResultUtil.successResult("清理成功, "+msg);
    }


    @RequestMapping("clearSettleDataByInsuranceOrderSn")
    @ResponseBody
    public Result clearSettleDataByInsuranceOrderSn(String insuranceOrderSn){
        if(StringUtils.isEmpty(insuranceOrderSn)){
            return ResultUtil.errorResult("", "淘汽保单号不能为空");
        }
        String logKey = "to clear shop settle data.";
        String operator = shiroBiz.getCurrentUserRealName();
        log.info("{} operator:{}, insuranceOrderSn:{}", logKey, operator, insuranceOrderSn);

        Date now = new Date();
        List<String> insuranceOrderSnList = new ArrayList<>();
        insuranceOrderSnList.add(insuranceOrderSn);

        /* 清理 settle_shop_check_detail */
        SettleShopCheckDetailQueryPO queryPO = new SettleShopCheckDetailQueryPO();
        queryPO.setInsuranceOrderSnList(insuranceOrderSnList);
        List<SettleShopCheckDetailDO> shopCheckDetailDOList = shopCheckDetailDOMapper.selectByCondition(queryPO);
        for(SettleShopCheckDetailDO shopCheckDetailDO : shopCheckDetailDOList){
            SettleShopCheckDetailDO updateDO = new SettleShopCheckDetailDO();
            updateDO.setId(shopCheckDetailDO.getId());
            updateDO.setGmtModified(now);
            updateDO.setModifier(TEST_CLEANER);
            updateDO.setIsDeleted(HAS_DELETED);
            shopCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
        }

        /* 清理 settle_form */
        Map<Integer, List<String>> formSnMap = new HashMap<>();
        List<SettleFormDO> settleFormDOList = settleFormDOMapper.getSettleFormListByInsuranceOrderSn(insuranceOrderSnList);
        for(SettleFormDO formDO : settleFormDOList){
            SettleFormDO updateDO = new SettleFormDO();
            updateDO.setId(formDO.getId());
            updateDO.setGmtModified(now);
            updateDO.setModifier(TEST_CLEANER);
            updateDO.setIsDeleted(HAS_DELETED);
            settleFormDOMapper.updateByPrimaryKeySelective(updateDO);

            Integer insuranceCompanyId = formDO.getInsuranceCompanyId();
            List<String> formSnList = formSnMap.get(insuranceCompanyId);
            if(formSnList==null){
                formSnList = new ArrayList<>();
                formSnMap.put(insuranceCompanyId, formSnList);
            }
            formSnList.add(formDO.getInsuredFormNo());
        }

        clearSettleDataCommon(insuranceOrderSnList, formSnMap, now);

        String msg = "insuranceOrderSn="+insuranceOrderSn
                +", formSnMap="+formSnMap.toString();

        log.info("{} {}", logKey, msg);
        return ResultUtil.successResult("清理成功, "+msg);
    }


    private void clearSettleDataCommon(List<String> insuranceOrderSnList, Map<Integer, List<String>> formSnMap, Date now){
        if(!insuranceOrderSnList.isEmpty()) {
            /* 清理 settle_service_check_detail */
            List<SettleServiceCheckDetailDO> serviceCheckDetailDOList =
                    serviceCheckDetailDOMapper.selectByInsuranceOrderSnList(insuranceOrderSnList);
            for (SettleServiceCheckDetailDO serviceCheckDetailDO : serviceCheckDetailDOList) {
                SettleServiceCheckDetailDO updateDO = new SettleServiceCheckDetailDO();
                updateDO.setId(serviceCheckDetailDO.getId());
                updateDO.setGmtModified(now);
                updateDO.setModifier(TEST_CLEANER);
                updateDO.setIsDeleted(HAS_DELETED);
                serviceCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
            }

            /* 清理 settle_car_owner_check_detail */
            List<SettleCarOwnerCheckDetailDO> carOwnerCheckDetailDOList =
                    carOwnerCheckDetailDOMapper.getCarOwnerCheckDetailForPageByInsuranceOrderSnList(insuranceOrderSnList);
            for (SettleCarOwnerCheckDetailDO carOwnerCheckDetailDO : carOwnerCheckDetailDOList) {
                SettleCarOwnerCheckDetailDO updateDO = new SettleCarOwnerCheckDetailDO();
                updateDO.setId(carOwnerCheckDetailDO.getId());
                updateDO.setGmtModified(now);
                updateDO.setModifier(TEST_CLEANER);
                updateDO.setIsDeleted(HAS_DELETED);
                carOwnerCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
            }
        }

        if(!formSnMap.isEmpty()){
            /* 清理 settle_company_check_detail */
            for(Map.Entry<Integer, List<String>> entry : formSnMap.entrySet()) {
                List<SettleCompanyCheckDetailDO> companyCheckDetailDOList =
                        companyCheckDetailDOMapper.selectBySettleFormSnList(entry.getValue(), entry.getKey());
                for (SettleCompanyCheckDetailDO companyCheckDetailDO : companyCheckDetailDOList) {
                    SettleCompanyCheckDetailDO updateDO = new SettleCompanyCheckDetailDO();
                    updateDO.setId(companyCheckDetailDO.getId());
                    updateDO.setGmtModified(now);
                    updateDO.setModifier(TEST_CLEANER);
                    updateDO.setIsDeleted(HAS_DELETED);
                    companyCheckDetailDOMapper.updateByPrimaryKeySelective(updateDO);
                }
            }
        }
    }
}
