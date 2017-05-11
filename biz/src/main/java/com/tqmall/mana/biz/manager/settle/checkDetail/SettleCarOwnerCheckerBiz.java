package com.tqmall.mana.biz.manager.settle.checkDetail;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.VO.settle.SettleCarOwnerBizPagingResult;
import com.tqmall.mana.beans.VO.settle.SettleCarOwnerCheckerDetailVO;
import com.tqmall.mana.beans.param.settle.SettleCarOwnerCheckerBizParam;
import com.tqmall.mana.beans.param.settle.SettleCarOwnerCheckerForBrilliantBizParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zengjinju on 17/1/21.
 */
public interface SettleCarOwnerCheckerBiz {
    SettleCarOwnerBizPagingResult carOwnerCheckerForERP(SettleCarOwnerCheckerBizParam param);

    Result<Boolean> updateConfirmMoneyPaidStatus(List<Integer> idList,String operator);

    /**
     * 审核支付状态操作
     *
     * @param id
     * @return
     */
    Result<Boolean> reviewPayStatus(Integer id, String operator);

    /**
     * 导出车主业务对账数据表
     * @param param
     */
    List<SettleCarOwnerCheckerDetailVO> exportSettleCarOwnerChekerList(SettleCarOwnerCheckerBizParam param);

    /**
     * 根据insuranceOrderSn更新settle_car_owner_check_detail表中的交强险应交保费，和商业险应交保费
     * @param
     * @return
     */
    Boolean updateByInsuranceOrderSn(SettleCarOwnerCheckerForBrilliantBizParam param);
}
