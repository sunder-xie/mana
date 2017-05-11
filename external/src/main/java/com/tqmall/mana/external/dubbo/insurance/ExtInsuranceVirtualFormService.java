package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.insurance.domain.result.InsuranceVirtualBasicDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualFormDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;

/**
 * Created by zhouheng on 16/12/24.
 */
public interface ExtInsuranceVirtualFormService {

    /**
     * 虚拟根据投保单或者保单id查询详情
     * @param formId
     * @return
     */
    InsuranceVirtualFormDTO getVirtualInsuranceFormDetailByFormId(Integer formId, Integer agentId) ;

    InsuranceVirtualBasicDTO selectVirtualFormByUserPackageId(Integer userPackageId);

    InsuranceVirtualBasicDTO getVirtualInsuranceFormDetailByBasicId(Integer basicId);

    PageEntityDTO<InsuranceVirtualFormDTO> getVirtualForms(String vehicleSn, Integer pageSize, Integer pageIndex);

}
