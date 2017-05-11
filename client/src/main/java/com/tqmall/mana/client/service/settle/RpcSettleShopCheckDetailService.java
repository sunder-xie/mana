package com.tqmall.mana.client.service.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.otherInsurance.CheckAllowanceDTO;
import com.tqmall.mana.client.beans.param.CheckAllowanceListRequestParam;
import com.tqmall.mana.client.beans.param.RecordAllowancePaymentInfoParam;

import java.util.List;
import java.util.Map;

/**
 * Created by zwb on 17/2/20.
 * 机滤补贴对账
 */
public interface RpcSettleShopCheckDetailService {
    /**
     * ERP---展示门店名称下拉框
     * @return
     */
    Result<Map<Integer,String>> showAllowanceAgentName(String agentName);

    /**
     * ERP---展示机滤请款状态下拉框
     * @return
     */
    Result<Map<Integer, String>> showAllowanceStatus();


    /**
     * ERP---根据orderSn获取请款状态
     * @param orderSn
     * @return
     */
    Result<Map<String,Integer>> getAllowanceStatusByOrderSn(List<String> orderSn);

    /**
     * ERP---查询保险机滤请款总额和列表
     *
     * @param requestParam
     * @return
     */
    Result<CheckAllowanceDTO> checkAllowanceList(CheckAllowanceListRequestParam requestParam);

    /**
     * ERP---更新保险机滤请款状态并回写请款信息
     * @return
     */
    Result<Boolean> updateAllowanceStatus(RecordAllowancePaymentInfoParam param);
}
