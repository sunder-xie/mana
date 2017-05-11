package com.tqmall.mana.external.dubbo.insurance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.service.insurance.cashcoupon.RpcInsuranceCashCouponService;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by huangzhangting on 17/4/19.
 */
@Slf4j
@Service
public class ExtCashCouponService {
    @Autowired
    private RpcInsuranceCashCouponService rpcInsuranceCashCouponService;

    /**
     * 现金券结算
     * @param cashCouponSnList
     */
    public void settleCashCoupon(List<String> cashCouponSnList){
        if(CollectionUtils.isEmpty(cashCouponSnList)){
            return;
        }
        log.info("settleCashCoupon param:{}", cashCouponSnList);
        try {
            Result<Void> result = rpcInsuranceCashCouponService.cashCouponSettle(cashCouponSnList);
            log.info("settleCashCoupon result:{}", JsonUtil.objectToStr(result));
        }catch (Exception e){
            log.error("settleCashCoupon error, param:"+cashCouponSnList, e);
        }
    }

}
