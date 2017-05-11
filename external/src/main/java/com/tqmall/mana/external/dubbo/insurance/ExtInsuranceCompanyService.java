package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.result.InsuranceCompanyDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceCompanyService;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/12/22.
 */
@Slf4j
@Service
public class ExtInsuranceCompanyService {
    @Autowired
    private RpcInsuranceCompanyService companyService;


    public List<InsuranceCompanyDTO> allCompanyList(){
        try {
            Result<List<InsuranceCompanyDTO>> result = companyService.allCompanyList();
            if (result.isSuccess()) {
                return result.getData();
            }
            log.info("get all insurance company failed, result:{}", JsonUtil.objectToStr(result));
        }catch (Exception e){
            log.error("get all insurance company error", e);
        }

        return new ArrayList<>();
    }

}
