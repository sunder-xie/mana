package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.insurance.domain.result.InsuranceFormDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/2.
 */
public interface ExtInsuranceFormService {
    /**
     * 根据车牌号查询投保单数据（使用默认条件就好，默认查询15条，根据创建时间desc）
     * 该方法非常特殊，仅在比价页面使用，后续有需求，再封装新的接口即可
     * @param vehicleSn
     * @return
     */
    List<InsuranceFormDTO> getInsuranceForms(String vehicleSn);


    /**
     *  根据保单ID和代理人id获取保单详细信息
     * @param formId
     * @param agentId
     * @return
     */
    InsuranceFormDTO getInsuranceFormDetail(Integer formId, Integer agentId);

    /**
     * 保单查询（有保单号的保单）
     * @param vehicleSn
     * @param pageSize
     * @param pageIndex
     * @return
     */
    PageEntityDTO<InsuranceFormDTO> getInsuredForms(String vehicleSn, Integer pageSize, Integer pageIndex);

    /**
     * 根据id查询保单详情
     * @param formId
     * @return
     */
    InsuranceFormDTO getInsuranceFormById(Integer formId);

}
