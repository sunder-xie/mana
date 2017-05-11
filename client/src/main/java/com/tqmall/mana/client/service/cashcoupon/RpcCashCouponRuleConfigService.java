package com.tqmall.mana.client.service.cashcoupon;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.cashcoupon.CreateRuleConfigResultDTO;

/**
 * Created by zhanghong on 17/4/11.
 */
public interface RpcCashCouponRuleConfigService {
    /**
     * 根据城市站编码获取配置规则(现金券面值、有效期限、商品类型和比例等)
     * @param cityCode
     * @return
     */
    Result<CreateRuleConfigResultDTO> getCreateRuleConfigInfo(String cityCode);

    /**
     * legend--根据城市编码判断是否开通现金券
     * @param cityCode
     * @param source
     * @return
     */
    Result<Boolean> getIsOpenByCityCode(String cityCode,String source);

}
