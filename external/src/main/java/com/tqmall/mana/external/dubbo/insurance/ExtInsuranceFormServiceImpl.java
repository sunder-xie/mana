package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.InsuranceBasicParam;
import com.tqmall.insurance.domain.result.InsuranceFormDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceFormService;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/12/2.
 */
@Slf4j
@Service
public class ExtInsuranceFormServiceImpl implements ExtInsuranceFormService {
    @Autowired
    private RpcInsuranceFormService rpcInsuranceFormService;

    @Override
    public List<InsuranceFormDTO> getInsuranceForms(String vehicleSn) {
        InsuranceBasicParam param = new InsuranceBasicParam();
        param.setBrilliantSource(true);
        param.setVehicleSn(vehicleSn.toUpperCase());
        param.setPageSize(10);

        Result<PageEntityDTO<InsuranceFormDTO>> result = rpcInsuranceFormService.selectApplyNoByNameAndVehicleSn(param);
        if(result.isSuccess()){
            PageEntityDTO<InsuranceFormDTO> pageEntityDTO = result.getData();
            if(pageEntityDTO != null){
                List<InsuranceFormDTO> formDTOList = pageEntityDTO.getRecordList();
                if(!CollectionUtils.isEmpty(formDTOList)){
                    return formDTOList;
                }
            }
        }

        log.info("selectApplyNoByNameAndVehicleSn failed, param:{}, result:{}", vehicleSn, JsonUtil.objectToStr(result));

        return new ArrayList<>();
    }

    @Override
    public InsuranceFormDTO getInsuranceFormDetail(Integer formId, Integer agentId) {

        Result<InsuranceFormDTO> result = rpcInsuranceFormService.getInsuranceFormDetail(formId, agentId);
        if(result.isSuccess()){
            return result.getData();
        }

        log.info("getInsuranceFormDetail failed, formId:{}, agentId:{}, result:{}",
                formId, agentId, JsonUtil.objectToStr(result));

        return null;
    }

    @Override
    public PageEntityDTO<InsuranceFormDTO> getInsuredForms(String vehicleSn, Integer pageSize, Integer pageIndex) {
        InsuranceBasicParam param = new InsuranceBasicParam();
        param.setBrilliantSource(true);
        param.setVehicleSn(vehicleSn);
        param.setPageSize(pageSize);
        param.setPageNum(pageIndex);

        Result<PageEntityDTO<InsuranceFormDTO>> result = rpcInsuranceFormService.selectInsuranceByNameAndVehicleSn(param);
        if(result.isSuccess()){
            return result.getData();
        }

        log.info("getInsuredForms failed, param:{}, result:{}",
                JsonUtil.objectToStr(param), JsonUtil.objectToStr(result));

        return null;
    }

    @Override
    public InsuranceFormDTO getInsuranceFormById(Integer formId) {
        Result<InsuranceFormDTO> result = rpcInsuranceFormService.getInsuranceFormDetail(formId);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("getInsuranceFormById failed, formId:{}, result:{}", formId, JsonUtil.objectToStr(result));
        return null;
    }

}
