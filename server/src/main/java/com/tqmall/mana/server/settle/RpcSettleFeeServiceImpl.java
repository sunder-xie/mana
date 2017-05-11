package com.tqmall.mana.server.settle;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.settle.CalculateTotalRebateResultBO;
import com.tqmall.mana.beans.param.settle.SettleFeeCalculatePO;
import com.tqmall.mana.beans.param.settle.UseCashCouponPO;
import com.tqmall.mana.biz.manager.settle.checkDetail.SettleShopCheckBiz;
import com.tqmall.mana.biz.manager.settle.config.SettleFormulaConfigBiz;
import com.tqmall.mana.client.beans.param.SettleFeeCalculateParam;
import com.tqmall.mana.client.beans.param.UseCouponParam;
import com.tqmall.mana.client.beans.settle.CalculateTotalRebateResultDTO;
import com.tqmall.mana.client.service.settle.RpcSettleFeeService;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by huangzhangting on 17/3/10.
 */
public class RpcSettleFeeServiceImpl implements RpcSettleFeeService {
    @Autowired
    private SettleShopCheckBiz shopCheckBiz;
    @Autowired
    private SettleFormulaConfigBiz formulaConfigBiz;


//    @Override
//    public Result<CalculateTotalRebateResultDTO> calculateTotalRebate(SettleFeeCalculateParam param) {
//        SettleFeeCalculatePO calculatePO = BdUtil.do2bo(param, SettleFeeCalculatePO.class);
//        CalculateTotalRebateResultBO resultBO = formulaConfigBiz.calculateTotalRebate(calculatePO);
//        return ResultUtil.successResult(BdUtil.do2bo(resultBO, CalculateTotalRebateResultDTO.class));
//    }

    @Override
    public Result useCashCoupon(UseCouponParam param) {
        UseCashCouponPO useCashCouponPO = BdUtil.do2bo(param, UseCashCouponPO.class);
        boolean flag = shopCheckBiz.useCashCoupon(useCashCouponPO);
        if(flag){
            return ResultUtil.successResult("", "用券成功");
        }
        return ResultUtil.errorResult("", "用券失败");
    }

}
