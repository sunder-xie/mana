package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.InsuranceVirtualBasicParam;
import com.tqmall.insurance.domain.result.InsuranceVirtualBasicDTO;
import com.tqmall.insurance.domain.result.InsuranceVirtualFormDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceVirtualFormService;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouheng on 16/12/24.
 */
@Service
@Slf4j
public class ExtInsuranceVirtualFormServiceImpl implements ExtInsuranceVirtualFormService {

    @Autowired
    private RpcInsuranceVirtualFormService rpcInsuranceVirtualFormService;


    @Override
    public InsuranceVirtualFormDTO getVirtualInsuranceFormDetailByFormId(Integer formId, Integer agentId) {

        Result<InsuranceVirtualFormDTO> result = rpcInsuranceVirtualFormService.getVirtualInsuranceFormDetailByFormId(formId,agentId);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("getVirtualInsuranceFormDetailByFormId failed,formId:{},agentId:{},result:{}",formId,agentId, JsonUtil.objectToStr(result));

        return null;
    }

    @Override
    public InsuranceVirtualBasicDTO selectVirtualFormByUserPackageId(Integer userPackageId) {
        Result<InsuranceVirtualBasicDTO> result = rpcInsuranceVirtualFormService.selectVirtualFormByUserPackageId(userPackageId);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("selectVirtualFormByUserPackageId failed, userPackageId:{}, result:{}", userPackageId, JsonUtil.objectToStr(result));
        return null;
    }

    @Override
    public InsuranceVirtualBasicDTO getVirtualInsuranceFormDetailByBasicId(Integer basicId) {
        Result<InsuranceVirtualBasicDTO> result =
                rpcInsuranceVirtualFormService.getVirtualInsuranceFormDetailByBasicId(basicId, null);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("getVirtualInsuranceFormDetailByBasicId failed, basicId:{}, result:{}", basicId, JsonUtil.objectToStr(result));
        return null;
    }

    @Override
    public PageEntityDTO<InsuranceVirtualFormDTO> getVirtualForms(String vehicleSn, Integer pageSize, Integer pageIndex) {
        InsuranceVirtualBasicParam param = new InsuranceVirtualBasicParam();
        param.setFlag(true);
        param.setPageSize(pageSize);
        param.setPageNum(pageIndex);
        param.setVehicleSn(vehicleSn);

        Result<PageEntityDTO<InsuranceVirtualFormDTO>> result = rpcInsuranceVirtualFormService.selectByNameAndVehicleSn(param);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("getVirtualForms failed, param:{}, result:{}",
                JsonUtil.objectToStr(param), JsonUtil.objectToStr(result));
        return null;
    }
}
