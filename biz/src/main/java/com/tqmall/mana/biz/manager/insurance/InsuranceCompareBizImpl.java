package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceCompareBO;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceItemBO;
import com.tqmall.mana.beans.BO.outInsurance.PingAnBO;
import com.tqmall.mana.beans.BO.outInsurance.RenBaoBO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceItemDO;
import com.tqmall.mana.biz.manager.outInsurance.PingAnInsuranceBiz;
import com.tqmall.mana.biz.manager.outInsurance.RenBaoInsuranceBiz;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 16/12/3.
 * 15:20
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
@Slf4j
public class InsuranceCompareBizImpl implements InsuranceCompareBiz {

    @Autowired
    private PingAnInsuranceBiz pingAnInsuranceBiz;
    @Autowired
    private RenBaoInsuranceBiz renBaoInsuranceBiz;
    @Autowired
    private InsuranceBiz insuranceBiz;

    @Override
    public Result<String> otherInsuranceCompare(InsuranceCompareBO compareBO) {
        Integer basicId = compareBO.getInsuranceBasicId();
        if(basicId == null || basicId < 1){
            return ResultUtil.errorResult("000","basicId cannot be null");
        }
        StringBuilder resultBuild = new StringBuilder();
        // 处理平安的车险
        PingAnBO pingAnBO = BdUtil.do2bo(compareBO, PingAnBO.class);
        Result<Map<ManaInsuranceItemDO,ManaInsuranceItemDO>> pingAnResult =  pingAnInsuranceBiz.getSYData(pingAnBO);
        if (!pingAnResult.isSuccess()){
            log.info("sad~!ping an insurance is fail.");
            resultBuild.append("get ping an insurance wrong.").append(pingAnResult.getMessage());
        }
        // 处理人保的车险
        RenBaoBO renBaoBO = BdUtil.do2bo(compareBO, RenBaoBO.class);
        Result<Map<ManaInsuranceItemDO,ManaInsuranceItemDO>> renResult =  renBaoInsuranceBiz.getSYData(renBaoBO);
        if (!renResult.isSuccess()){
            log.info("sad~!ren bao insurance is fail.");
            resultBuild.append("get renbao  insurance wrong.").append(renResult.getMessage());
        }

        // 保存结果
        Boolean saveResult = saveCompareResult(compareBO,pingAnResult.getData(),renResult.getData());
//        Boolean saveResult = saveCompareResult(compareBO,renResult.getData());
        if(saveResult){
            return ResultUtil.successResult(resultBuild.toString());
        }
        return ResultUtil.errorResult("000","save to mysql wrong");
    }

    /*==== 2级private====*/
    private Boolean saveCompareResult(InsuranceCompareBO compareBO,Map<ManaInsuranceItemDO,ManaInsuranceItemDO>... otherMap){
        // 拼接平安车险结果和人保结果，anxinName+basicId:ManaInsuranceItemDO
        Map<ManaInsuranceItemDO,ManaInsuranceItemDO> needConsoleMap = new HashMap<>();
        for(Map<ManaInsuranceItemDO,ManaInsuranceItemDO> oneMap:otherMap) {
            if(oneMap != null) {
                consoleOtherInsurance(oneMap, needConsoleMap);
            }
        }

        List<ManaInsuranceItemDO> saveList = new ArrayList<>();
        Map<ManaInsuranceItemDO,ManaInsuranceItemDO> upMap = new HashMap<>();//Map<upDO,whereDO>
        //结果进行分类更新和插入
        disUpOrInsert(compareBO,needConsoleMap,saveList,upMap);

        // 更新数据
        for(Map.Entry<ManaInsuranceItemDO,ManaInsuranceItemDO> upMapEntry:upMap.entrySet()){
            ManaInsuranceItemDO upDO = upMapEntry.getKey();
            ManaInsuranceItemDO whereDO = upMapEntry.getValue();
            insuranceBiz.updateInsuranceItem(upDO,whereDO);
        }
        //插入数据
        insuranceBiz.saveInsuranceItem(saveList);
        return true;

    }


    /*==== 3级private====*/

    //结果进行分类更新和插入
    private void disUpOrInsert(InsuranceCompareBO compareBO,Map<ManaInsuranceItemDO,ManaInsuranceItemDO> needConsoleMap,
                               List<ManaInsuranceItemDO> saveList,Map<ManaInsuranceItemDO,ManaInsuranceItemDO> upMap){

        Integer basicId = compareBO.getInsuranceBasicId();
        List<InsuranceItemBO> amountList = compareBO.getAmountList();
        for (InsuranceItemBO insuranceItemBO : amountList) {
            String amountName = insuranceItemBO.getInsuranceName().replace("（", "(").replace("）", ")");
            ManaInsuranceItemDO keyDO = new ManaInsuranceItemDO();
            keyDO.setInsuranceBasicId(basicId);
            keyDO.setInsuranceName(amountName);
            //比较出来的车险
            ManaInsuranceItemDO resultDO = needConsoleMap.get(keyDO);
            if(resultDO == null){
                log.error("needConsoleMap not have key:[basicId:{},amountName:{}],needConsoleMap is [].",basicId,amountName, JsonUtil.objectToStr(needConsoleMap));
                resultDO = new ManaInsuranceItemDO();
                resultDO.setInsuranceName(amountName);
                resultDO.setRenBaoRemark(ManaInsuranceItemDO.NO_DO_REMARKS);
                resultDO.setPingAnRemark(ManaInsuranceItemDO.NO_DO_REMARKS);
                resultDO.setInsuranceFeeRenBao(BigDecimal.ZERO);
                resultDO.setInsuranceFeePingAn(BigDecimal.ZERO);
            }
            // 判断是否数据库是否存在
            Result<ManaInsuranceItemDO> mysqlDOResult = insuranceBiz.getInsuranceItemDOByBasicIdAndName(basicId,amountName);
            if(!mysqlDOResult.isSuccess()){
                log.info("insuranceBiz.getInsuranceItemDOByBasicIdAndName error.");
            }else {
                ManaInsuranceItemDO mysqlDO = mysqlDOResult.getData();
                if(mysqlDO != null){
                    // 数据库存在数据,获得需要更新的项
                    ManaInsuranceItemDO upDO = needUpContent(mysqlDO, resultDO);
                    if(upDO != null){
                        upMap.put(upDO,keyDO);
                    }
                    continue;
                }
            }
            // 插入数据 基本参数初始化
            BigDecimal insuranceAmount = insuranceItemBO.getInsuranceAmount();
            BigDecimal insuranceFee = insuranceItemBO.getInsuranceFee();

            resultDO.setInsuranceBasicId(basicId);
            resultDO.setInsuranceType(ManaInsuranceItemDO.INSURANCE_TYPE_SY);
            resultDO.setIsDeductible(insuranceItemBO.getDeductible());
            resultDO.setInsuranceAmount(insuranceAmount == null ? BigDecimal.ZERO:insuranceAmount);
            resultDO.setInsuranceFee(insuranceFee == null ? BigDecimal.ZERO:insuranceFee);
            saveList.add(resultDO);
        }
    }


    // 将其他的车险数据拼接到要处理的map中。例如 平安、人保
    private void consoleOtherInsurance(Map<ManaInsuranceItemDO, ManaInsuranceItemDO> otherMap, Map<ManaInsuranceItemDO, ManaInsuranceItemDO> needConsoleMap){
        //        anxinName+basicId:ManaInsuranceItemDO
        for (Map.Entry<ManaInsuranceItemDO,ManaInsuranceItemDO> pingAnEntry : otherMap.entrySet()){
            ManaInsuranceItemDO keyDO = pingAnEntry.getKey();
            ManaInsuranceItemDO saveDO = pingAnEntry.getValue();

            ManaInsuranceItemDO needDO;
            if(!needConsoleMap.containsKey(keyDO)){
                needDO = saveDO;
                // 如果之前这个险类不存在，则存入
                needConsoleMap.put(keyDO,needDO);
            }else {
                needDO = needConsoleMap.get(keyDO);
            }
            //平安的
            BigDecimal needPingAnFee = needDO.getInsuranceFeePingAn();
            String needPingAnRemarks = needDO.getPingAnRemark();
            // 人保
            BigDecimal needRenFee = needDO.getInsuranceFeeRenBao();
            String needRenRemarks = needDO.getRenBaoRemark();

            //设置值
            //平安
            if(needPingAnFee == null || BigDecimal.ZERO.compareTo(needPingAnFee) == 0){
                BigDecimal savePingAnFee = saveDO.getInsuranceFeePingAn();
                // 如果存在平安数据，则平安数据，否则表示无此项
                needPingAnFee = savePingAnFee == null?BigDecimal.ZERO:savePingAnFee;
                needDO.setInsuranceFeePingAn(needPingAnFee);
            }
            if(isEmptyRemarks(needPingAnRemarks)){
                String savePingANRemarks = saveDO.getPingAnRemark();
                // 当保存的备注为null或者说未处理时
                    needPingAnRemarks = isEmptyRemarks(savePingANRemarks) ? ManaInsuranceItemDO.NO_DO_REMARKS : savePingANRemarks;
                needDO.setPingAnRemark(needPingAnRemarks);
            }
            //人保
            if(needRenFee == null || BigDecimal.ZERO.compareTo(needRenFee) == 0){
                BigDecimal saveFee = saveDO.getInsuranceFeeRenBao();
                needRenFee = saveFee == null?BigDecimal.ZERO:saveFee;
                needDO.setInsuranceFeeRenBao(needRenFee);
            }
            if(isEmptyRemarks(needRenRemarks)){
                String saveRemarks = saveDO.getRenBaoRemark();
                needRenRemarks = isEmptyRemarks(saveRemarks) ? ManaInsuranceItemDO.NO_DO_REMARKS : saveRemarks;
                needDO.setRenBaoRemark(needRenRemarks);
            }

        }
    }



    /*==== 4级private====*/
    private Boolean isEmptyRemarks(String remarks){
        return remarks == null || remarks.equals(ManaInsuranceItemDO.NO_DO_REMARKS);
    }
    private ManaInsuranceItemDO needUpContent(ManaInsuranceItemDO oldDO,ManaInsuranceItemDO newDO){
        Boolean isChanged = Boolean.FALSE;

        BigDecimal newRenBaoFee = newDO.getInsuranceFeeRenBao();
        String newRenBaoRemarks = newDO.getRenBaoRemark();
        BigDecimal newPingFee = newDO.getInsuranceFeePingAn();
        String newPingRemarks = newDO.getPingAnRemark();

        ManaInsuranceItemDO upDO = new ManaInsuranceItemDO();
        //不相等
        if(oldDO.getInsuranceFeeRenBao().compareTo(newRenBaoFee) != 0){
            isChanged = true;
            upDO.setInsuranceFeeRenBao(newRenBaoFee);
        }
        if(!oldDO.getRenBaoRemark().equals(newRenBaoRemarks)){
            isChanged = true;
            upDO.setRenBaoRemark(newRenBaoRemarks);
        }
        //不相等
        if(oldDO.getInsuranceFeePingAn().compareTo(newPingFee) != 0){
            isChanged = true;
            upDO.setInsuranceFeePingAn(newPingFee);
        }
        if(!oldDO.getPingAnRemark().equals(newPingRemarks)){
            isChanged = true;
            upDO.setPingAnRemark(newPingRemarks);
        }
        if(isChanged){
            return upDO;
        }
        return null;
    }
}
