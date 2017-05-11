package com.tqmall.mana.client.service.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.param.SettleFeeCalculateParam;
import com.tqmall.mana.client.beans.param.UseCouponParam;
import com.tqmall.mana.client.beans.settle.CalculateTotalRebateResultDTO;

/**
 * Created by huangzhangting on 17/3/10.
 */
public interface RpcSettleFeeService {

    /**
     * 计算总返利
     * @param param
     * @return
     */
//    Result<CalculateTotalRebateResultDTO> calculateTotalRebate(SettleFeeCalculateParam param);

    /**
     * 使用现金券接口
     * @param param
     * @return
     */
    Result useCashCoupon(UseCouponParam param);

}
