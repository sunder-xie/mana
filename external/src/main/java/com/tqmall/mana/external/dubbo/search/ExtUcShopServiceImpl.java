package com.tqmall.mana.external.dubbo.search;

import com.tqmall.mana.component.util.JsonUtil;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.common.result.Result;
import com.tqmall.search.dubbo.client.ucenter.param.InsuranceShopParam;
import com.tqmall.search.dubbo.client.ucenter.param.UcShopParam;
import com.tqmall.search.dubbo.client.ucenter.result.InsuranceShopResult;
import com.tqmall.search.dubbo.client.ucenter.result.UcShopDTO;
import com.tqmall.search.dubbo.client.ucenter.service.UcShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by zhouheng on 16/12/22.
 */
@Slf4j
@Service
public class ExtUcShopServiceImpl implements ExtUcShopService {

    @Autowired
    private UcShopService ucShopService;


    @Override
    public Page<UcShopDTO> getUcShops(UcShopParam var1, PageableRequest var2) {

        Result<Page<UcShopDTO>> result = ucShopService.getUcShops(var1,var2);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("getUcShops failed , result : ",result);

        return null;
    }

    @Override
    public Page<InsuranceShopResult> getInsuranceShops(InsuranceShopParam param, PageableRequest pageableRequest) {
        log.info("getInsuranceShops from search, param:{}, page:{}", JsonUtil.objectToStr(param), JsonUtil.objectToStr(pageableRequest));
        Result<Page<InsuranceShopResult>> result = ucShopService.getInsuranceShops(param, pageableRequest);
        if(result.isSuccess()){
            return result.getData();
        }
        log.info("getInsuranceShops from search failed, result:{}", JsonUtil.objectToStr(result));
        return null;
    }
}
