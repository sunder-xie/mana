package com.tqmall.mana.external.dubbo.finance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.common.errorcode.support.SourceKey;
import com.tqmall.finance.model.param.order.CashBackOrderParam;
import com.tqmall.finance.model.param.order.WithdrawRewardAountParam;
import com.tqmall.finance.service.insurance.FcInsuranceService;
import com.tqmall.mana.beans.param.settle.finance.CashBackPO;
import com.tqmall.mana.beans.param.settle.finance.WithdrawRewardPO;
import com.tqmall.mana.component.util.BdUtil;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangzhangting on 17/2/7.
 */
@Slf4j
@Service
public class ExtFcInsuranceService {
    @Autowired
    private FcInsuranceService fcInsuranceService;


    /** 奖励金生效 */
    public boolean cashBack(CashBackPO cashBackPO){
        log.info("cashBack param:{}", JsonUtil.objectToStr(cashBackPO));
        try {
            CashBackOrderParam param = BdUtil.do2bo(cashBackPO, CashBackOrderParam.class);
            param.setUid(param.getShopId());
            param.setSource(SourceKey.INSURANCE.getKey());

            Result<Boolean> result = fcInsuranceService.cashBack(param);
            if(result.isSuccess()){
                return true;
            }
            log.info("cashBack failed, result:{}", JsonUtil.objectToStr(result));
        }catch (Exception e){
            log.error("invoke finance cashBack error", e);
        }
        return false;
    }


    /** 奖励金提现 */
    public boolean withdrawReward(WithdrawRewardPO withdrawRewardPO){
        log.info("withdrawReward param:{}", JsonUtil.objectToStr(withdrawRewardPO));
        try {
            WithdrawRewardAountParam param = BdUtil.do2bo(withdrawRewardPO, WithdrawRewardAountParam.class);
            param.setUid(withdrawRewardPO.getShopId());
            param.setSource(SourceKey.INSURANCE.getKey());

            Result<Boolean> result = fcInsuranceService.withdrawRewardAmount(param);
            if(result.isSuccess()){
                return true;
            }
            log.info("withdrawReward failed, result:{}", JsonUtil.objectToStr(result));
        }catch (Exception e){
            log.error("invoke finance withdrawRewardAmount error", e);
        }
        return false;
    }

}
