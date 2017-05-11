package com.tqmall.mana.biz.manager.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.servicepackage.CheckServicePackageParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.ServicePackageParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.serviceitem.ServiceItemParam;
import com.tqmall.insurance.domain.result.InsuranceServiceItemDTO;
import com.tqmall.insurance.domain.result.InsuranceServicePackageDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.mana.beans.BO.insurance.ServiceItemBO;
import com.tqmall.mana.beans.BO.insurance.ServicePackageBO;
import com.tqmall.mana.beans.param.settle.insurance.CheckServicePackageBOParam;
import com.tqmall.mana.component.exception.BusinessCheckException;
import com.tqmall.mana.component.exception.BusinessException;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import com.tqmall.mana.external.dubbo.insurance.ExtInsurancePackageService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouheng on 17/2/24.
 */
@Data
@Service
public class InsurancePackageBizImpl implements InsurancePackageBiz {

    @Autowired
    private ExtInsurancePackageService extInsurancePackageService;

    @Override
    public Result<Boolean> createServicePackage(ServicePackageBO servicePackageBO) {

        ServicePackageParam servicePackageParam = convertBOtoDOParam(servicePackageBO);
        Result<Boolean> result = extInsurancePackageService.createServicePackage(servicePackageParam);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        return result;
    }

    @Override
    public Result<Boolean> updateServicePackageConfig(ServicePackageBO servicePackageBO) {
        ServicePackageParam servicePackageParam = convertBOtoDOParam(servicePackageBO);
        Result<Boolean> result = extInsurancePackageService.updateServicePackageConfig(servicePackageParam);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        return result;
    }

    @Override
    public PagingResult<ServicePackageBO> getServicePackageList(CheckServicePackageBOParam checkServicePackageBOParam) {
        String packageName = checkServicePackageBOParam.getPackageName();
        if("".equals(packageName)){
            checkServicePackageBOParam.setPackageName(null);
        }
        CheckServicePackageParam checkServicePackageParam = BdUtil.do2bo(checkServicePackageBOParam,CheckServicePackageParam.class);
        Result<PageEntityDTO<InsuranceServicePackageDTO>> result = extInsurancePackageService.getServicePackageList(checkServicePackageParam);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }

        PageEntityDTO<InsuranceServicePackageDTO> pageEntityDTO = result.getData();
        List<InsuranceServicePackageDTO> packageDTOList = pageEntityDTO.getRecordList();
        if(CollectionUtils.isEmpty(packageDTOList)){
            throw new BusinessException("暂无查询数据");
        }

        List<ServicePackageBO> packageBOList = convertPackageDTOtoBOList(packageDTOList);
        int total = pageEntityDTO.getTotalNum();
        return PagingResult.wrapSuccessfulResult(packageBOList,total);
    }

    private CheckServicePackageParam convertBOParamToDO(CheckServicePackageBOParam checkServicePackageBOParam) {

        CheckServicePackageParam checkServicePackageParam = BdUtil.do2bo(checkServicePackageBOParam,CheckServicePackageParam.class);

        return checkServicePackageParam;

    }

    @Override
    public Result<ServicePackageBO> getServicePackageConfig(Integer id) {
        if(id == null){
            throw new BusinessCheckException("000","参数异常");
        }
        Result<InsuranceServicePackageDTO> result = extInsurancePackageService.getServicePackageConfig(id);
        if(!result.isSuccess()){
            return ResultUtil.errorResult("001",result.getMessage());
        }
        InsuranceServicePackageDTO servicePackageDTO = result.getData();
        ServicePackageBO servicePackageBO = convertPackageDTOtoBO(servicePackageDTO);
        return Result.wrapSuccessfulResult(servicePackageBO);
    }

    private List<ServicePackageBO> convertPackageDTOtoBOList(List<InsuranceServicePackageDTO> packageDTOList){
        if(CollectionUtils.isEmpty(packageDTOList)){
            throw new BusinessCheckException("000","数据转换参数错误");
        }
        List<ServicePackageBO> packageBOList = new ArrayList<>();
        for (InsuranceServicePackageDTO packageDTO: packageDTOList) {
            ServicePackageBO servicePackageBO = convertPackageDTOtoBO(packageDTO);
            packageBOList.add(servicePackageBO);
        }
        return packageBOList;
    }

    private ServicePackageBO convertPackageDTOtoBO(InsuranceServicePackageDTO servicePackageDTO){

        ServicePackageBO servicePackageBO = BdUtil.do2bo(servicePackageDTO,ServicePackageBO.class);
        List<InsuranceServiceItemDTO> itemDTOList = servicePackageDTO.getServiceItemList();
        if(!CollectionUtils.isEmpty(itemDTOList)){
            List<ServiceItemBO> itemBOList = new ArrayList<>();
            for (InsuranceServiceItemDTO itemDTO:itemDTOList) {
                ServiceItemBO itemBO = BdUtil.do2bo(itemDTO,ServiceItemBO.class);
                itemBO.setItemTimes(itemDTO.getServiceTimes());
                itemBOList.add(itemBO);
            }
            servicePackageBO.setServiceItemParams(itemBOList);
        }
        return servicePackageBO;
    }

    @Override
    public Result<Boolean> updateServicePackageStatus(Integer id, Integer packageStatus) {
        if(id == null || packageStatus == null){
            throw new BusinessCheckException("000","参数异常");
        }
        Result<Boolean> result = extInsurancePackageService.updateServicePackageStatus(id,packageStatus);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        return result;
    }

    @Override
    public Result<Boolean> deleteServicePackage(Integer id) {
        if(id == null){
            throw new BusinessCheckException("000","参数异常");
        }
        Result<Boolean> result = extInsurancePackageService.deleteServicePackage(id);
        if(!result.isSuccess()){
            throw new BusinessException(result.getMessage());
        }
        return result;
    }

    private ServicePackageParam convertBOtoDOParam(ServicePackageBO servicePackageBO){
        ServicePackageParam servicePackageParam = BdUtil.do2bo(servicePackageBO,ServicePackageParam.class);
        List<ServiceItemBO> serviceItemParams = servicePackageBO.getServiceItemParams();
        if(!CollectionUtils.isEmpty(serviceItemParams)){
            List<ServiceItemParam> serviceItemParamsList = BdUtil.do2bo4List(serviceItemParams,ServiceItemParam.class);
            servicePackageParam.setServiceItemParams(serviceItemParamsList);
        }else{
            throw new BusinessCheckException("002","数据不完整");
        }
        return servicePackageParam;
    }
}
