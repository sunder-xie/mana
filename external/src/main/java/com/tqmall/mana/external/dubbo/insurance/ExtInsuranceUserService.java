package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.InsuranceUserServicePackageDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceUserService;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangzhangting on 17/1/4.
 */
@Slf4j
@Service
public class ExtInsuranceUserService {
    @Autowired
    private RpcInsuranceUserService rpcInsuranceUserService;


    public InsuranceUserServicePackageDTO getPackageById(Integer packageId){
        if (packageId==null || packageId<1){
            log.info("getPackageById illegal param, packageId:{}", packageId);
            return null;
        }
        Result<InsuranceUserServicePackageDTO> result = rpcInsuranceUserService.getUserServicePackageInfo(packageId);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("getUserServicePackageInfo failed, packageId:{}, result:{}", packageId, JsonUtil.objectToStr(result));
        return null;
    }

}
