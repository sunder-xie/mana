package com.tqmall.mana.external.dubbo.search;

import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.ucenter.param.InsuranceShopParam;
import com.tqmall.search.dubbo.client.ucenter.param.UcShopParam;
import com.tqmall.search.dubbo.client.ucenter.result.InsuranceShopResult;
import com.tqmall.search.dubbo.client.ucenter.result.UcShopDTO;
import org.springframework.data.domain.Page;

/**
 * Created by zhouheng on 16/12/22.
 */
public interface ExtUcShopService {

    /**
     * 通过参数或去门店信息
     * @param var1
     * @param var2
     * @return
     */
    Page<UcShopDTO> getUcShops(UcShopParam var1, PageableRequest var2);


    /**
     * 保险门店搜索
     * @param param
     * @param pageableRequest
     * @return
     */
    Page<InsuranceShopResult> getInsuranceShops(InsuranceShopParam param, PageableRequest pageableRequest);

}
