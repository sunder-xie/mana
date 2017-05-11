package com.tqmall.mana.biz.util;

import com.tqmall.mana.beans.entity.insurance.OtherInsuranceRelationDO;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zxg on 16/12/2.
 * 14:05
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class OtherRelationUtils {
    // 安心名称对应的数据
    private static Map<String,OtherInsuranceRelationDO> insuranceRelationDOMap = new ConcurrentHashMap<>();
    // 平安的 字段名对应的数据
    private static Map<String,OtherInsuranceRelationDO> pingAnRelationDOMap = new ConcurrentHashMap<>();
    // 人保的字段名 对应的数据
    private static Map<String,OtherInsuranceRelationDO> renRelationDOMap = new ConcurrentHashMap<>();

    //根据安心的名称获得对应数据
    public static OtherInsuranceRelationDO getInsuranceByAnXinName(String anxinName){
        if(insuranceRelationDOMap.keySet().contains(anxinName)){
            return insuranceRelationDOMap.get(anxinName);
        }
        return null;
    }

    //根据平安的字段获得数据
    public static OtherInsuranceRelationDO getInsuranceByPingAn(String pingAnName){
        if(pingAnRelationDOMap.keySet().contains(pingAnName)){
            return pingAnRelationDOMap.get(pingAnName);
        }
        return null;
    }

    //根据人保的字段获得数据
    public static OtherInsuranceRelationDO getInsuranceByRenBao(String renName){
        if(renRelationDOMap.keySet().contains(renName)){
            return renRelationDOMap.get(renName);
        }
        return null;
    }


    public static Map<String,Object> getMap(){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("insuranceMap",insuranceRelationDOMap);
        resultMap.put("pingMap",pingAnRelationDOMap);
        resultMap.put("renMap",renRelationDOMap);
        return resultMap;
    }

    public static void setInsuranceRelationDOMap(List<OtherInsuranceRelationDO> list){
        Map<String,OtherInsuranceRelationDO> insuranceMap = new ConcurrentHashMap<>();
        Map<String,OtherInsuranceRelationDO> pingMap = new ConcurrentHashMap<>();
        Map<String,OtherInsuranceRelationDO> renMap = new ConcurrentHashMap<>();

        for(OtherInsuranceRelationDO insuranceRelationDO:list){
            OtherInsuranceRelationDO saveInsurance = new OtherInsuranceRelationDO();

            String anxinInsuranceName = insuranceRelationDO.getAnxinInsuranceName();
            String pingAnDeductible = insuranceRelationDO.getPingAnDeductible();
            String pingAnInsuranceName = insuranceRelationDO.getPingAnInsuranceName();
            String renDeductible = insuranceRelationDO.getRenDeductible();
            String renInsuranceName = insuranceRelationDO.getRenInsuranceName();
            saveInsurance.setAnxinInsuranceName(anxinInsuranceName);

            saveInsurance.setPingAnDeductible(pingAnDeductible);
            saveInsurance.setPingAnInsuranceName(pingAnInsuranceName);
            saveInsurance.setRenDeductible(renDeductible);
            saveInsurance.setRenInsuranceName(renInsuranceName);

            insuranceMap.put(anxinInsuranceName, saveInsurance);

            if(!StringUtils.isEmpty(pingAnDeductible)) {
                pingMap.put(pingAnDeductible,saveInsurance);
            }
            if(!StringUtils.isEmpty(pingAnInsuranceName)) {
                pingMap.put(pingAnInsuranceName,saveInsurance);
            }
            if(!StringUtils.isEmpty(renDeductible)) {
                renMap.put(renDeductible,saveInsurance);
            }
            if(!StringUtils.isEmpty(renInsuranceName)) {
                renMap.put(renInsuranceName,saveInsurance);
            }
        }
        insuranceRelationDOMap = insuranceMap;
        pingAnRelationDOMap = pingMap;
        renRelationDOMap = renMap;
    }
}
