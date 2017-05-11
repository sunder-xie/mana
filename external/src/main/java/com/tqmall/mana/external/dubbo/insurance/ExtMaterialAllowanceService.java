package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.insurance.domain.result.CheckAllowanceDTO;

/**
 * Created by huangzhangting on 17/3/16.
 */
public interface ExtMaterialAllowanceService {
    /**
     * 根据订单号查询材料补贴
     * @param orderSn
     * @return
     */
    CheckAllowanceDTO checkAllowanceList(String orderSn);
}
