package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.servicepackage.CheckServicePackageParam;
import com.tqmall.insurance.domain.param.insurance.servicepackage.ServicePackageParam;
import com.tqmall.insurance.domain.result.InsuranceServicePackageDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.service.insurance.RpcPackageService;
import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.mana.component.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhouheng on 17/2/24.
 */
@Slf4j
@Service
public class ExtInsurancePackageServiceImpl implements ExtInsurancePackageService {

    @Autowired
    private RpcPackageService rpcPackageService;

    @Override
    public Result<Boolean> createServicePackage(ServicePackageParam servicePackageParam) {
        log.info("createServicePackage ,param:{} ", JsonUtil.objectToStr(servicePackageParam));
        return rpcPackageService.createServicePackage(servicePackageParam);
    }

    @Override
    public Result<Boolean> updateServicePackageConfig(ServicePackageParam servicePackageParam) {
        log.info("updateServicePackageConfig ,param:{} ", JsonUtil.objectToStr(servicePackageParam));
        return rpcPackageService.updateServicePackageConfig(servicePackageParam);
    }

    @Override
    public Result<PageEntityDTO<InsuranceServicePackageDTO>> getServicePackageList(CheckServicePackageParam checkServicePackageParam) {
        log.info("getServicePackageList,param:{}", JsonUtil.objectToStr(checkServicePackageParam));
        return rpcPackageService.getServicePackageList(checkServicePackageParam);
    }

    @Override
    public Result<InsuranceServicePackageDTO> getServicePackageConfig(Integer id) {
        log.info("getServicePackageConfig,param:{}", id);
        if (id == null) {
            log.info("getServicePackageConfig param error");
            return ResultUtil.errorResult("001","参数异常");
        }
        return rpcPackageService.getServicePackageConfig(id);
    }

    @Override
    public Result<Boolean> updateServicePackageStatus(Integer id, Integer packageStatus) {
        log.info("updateServicePackageStatus,param:id:{},status:{}", id, packageStatus);
        if (id == null || packageStatus == null) {
            log.info("updateServicePackageStatus param error ");
            return ResultUtil.errorResult("001","参数异常");
        }
        return rpcPackageService.updateServicePackageStatus(id, packageStatus);
    }

    @Override
    public Result<Boolean> deleteServicePackage(Integer id) {
        log.info("deleteServicePackage,param:{}", id);
        if (id == null) {
            log.info("deleteServicePackage param error");
            return ResultUtil.errorResult("0","参数异常");
        }
        return rpcPackageService.deleteServicePackage(id);
    }
}
