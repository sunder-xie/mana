package com.tqmall.mana.biz.manager.settle.checkDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 17/2/20.
 */
public interface SettleShopCheckDetailBiz {

    /**
     * erp-查询保险机滤门店名称列表
     **/
    Map<Integer, String> showAllowanceAgentName(String shopName);

    /**
     * ERP---根据orderSn获取请款状态
     *
     * @param orderSn
     * @return
     */
    Map<String, Integer> getAllowanceStatusByOrderSn(List<String> orderSn);


}
