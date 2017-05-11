package com.tqmall.mana.client.service.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.client.beans.param.SettleCarOwnerCheckerForBrilliantParam;
import com.tqmall.mana.client.beans.param.SettleCarOwnerCheckerParam;
import com.tqmall.mana.client.beans.settle.SettleCarOwnerCheckerDetailDTO;
import com.tqmall.mana.client.beans.settle.SettleCarOwnerPagingResult;

import java.util.List;

/**
 * Created by zengjinju on 17/1/20.
 */
public interface RpcSettleCarOwnerCheckerService {

    Result<SettleCarOwnerPagingResult> carOwnerCheckerForERP(SettleCarOwnerCheckerParam settleCarOwnerCheckerParam);

    /**
     *车主对账确认收款按钮
     * @param idList
     * @return
     */
    Result<Boolean> updateConfirmMoneyPaidStatus(List<Integer> idList,String operator);

    /**
     * 审核支付状态
     * @param id
     * @return
     */
    Result<Boolean> reviewPayStatus(Integer id,String operator);

    /**
     * 导出数据
     * @param param
     * @return
     */
    Result<List<SettleCarOwnerCheckerDetailDTO>> exportSettleCarOwnerChekerList(SettleCarOwnerCheckerParam param);

    /**
     * 根据insuranceOrderSn更新settle_car_owner_check_detail表中的交强险应交保费，和商业险应交保费
     * @param
     * @return
     */
    Result<Boolean> updateByInsuranceOrderSn(SettleCarOwnerCheckerForBrilliantParam param);




}
