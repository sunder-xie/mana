package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.insurance.InsuranceCompanyBO;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceFormBO;
import com.tqmall.mana.beans.VO.InsuranceInfoVO;
import com.tqmall.mana.beans.VO.InsurancePriceParityVO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceItemDO;
import com.tqmall.mana.beans.param.CommonVOPageParam;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/2.
 */
public interface InsuranceBiz {

    Result<List<InsurancePriceParityVO>> getInsurancePriceParityVOList(String vehicleSn);

    Result<List<ManaInsuranceItemDO>> getInsuranceItemDOList(Integer insuranceBasicId);

    // 通过id和insuranceName，获得对象，若无则返回false:null--用于外部判断
    Result<ManaInsuranceItemDO> getInsuranceItemDOByBasicIdAndName(Integer insuranceBasicId, String insuranceName);


    /*===数据操作 更新 插入====*/
    // 根据条件进行更新
    Result<String> updateInsuranceItem(ManaInsuranceItemDO upDO, ManaInsuranceItemDO whereDO);

    // 批量插入
    Result<String> saveInsuranceItem(List<ManaInsuranceItemDO> saveList);

    /**
     * 通过vehicleId获取车辆历史保单信息列表
     *
     * @param commonVOPageParam
     * @return
     */
    Result<List<InsuranceFormBO>> getInsuranceFormPagingResult(CommonVOPageParam commonVOPageParam);

    /**
     * 通过保单ID和代理人id获取保单信息详情
     *
     * @param formId
     * @param agentId
     * @return
     */
    Result<InsuranceFormBO> getInsuranceFormInfo(Integer formId, Integer agentId);

    /**
     * 获取所有保险公司列表
     *
     * @return
     */
    List<InsuranceCompanyBO> getAllCompanyList();

    /**
     * 通过保险公司id获取保险公司名称
     *
     * @param insureCompanyId
     * @return
     */
    Result<String> getInsureCompanyNameById(Integer insureCompanyId);

    /** =============== hzt ================ */
    /**
     * 根据车辆id获取历史保单数据，包含服务包数据
     *
     * @param vehicleId
     * @return
     */
    Result<List<InsuranceInfoVO>> getInsuranceInfoVOList(Integer vehicleId);


}
