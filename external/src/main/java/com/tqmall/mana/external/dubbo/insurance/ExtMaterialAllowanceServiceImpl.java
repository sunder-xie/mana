package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.CheckAllowanceListRequestParam;
import com.tqmall.insurance.domain.result.CheckAllowanceDTO;
import com.tqmall.insurance.service.insurance.RpcPackageAllowanceService;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangzhangting on 17/3/16.
 */
@Slf4j
@Service
public class ExtMaterialAllowanceServiceImpl implements ExtMaterialAllowanceService {
    @Autowired
    private RpcPackageAllowanceService allowanceService;

    @Override
    public CheckAllowanceDTO checkAllowanceList(String orderSn) {
        CheckAllowanceListRequestParam param = new CheckAllowanceListRequestParam();
        param.setOrderSn(orderSn);
        param.setPageSize(1);
        param.setPageNum(1);
        Result<CheckAllowanceDTO> result = allowanceService.checkAllowanceList(param);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("checkAllowanceList failed, param:{}, result:{}",
                JsonUtil.objectToStr(param), JsonUtil.objectToStr(result));
        return null;
    }
}
