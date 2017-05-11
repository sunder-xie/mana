package com.tqmall.mana.server.cashcoupon;

import com.tqmall.core.common.entity.Result;
import com.tqmall.mana.beans.BO.cashcoupon.CashCouponRuleConfigBO;
import com.tqmall.mana.biz.manager.coupon.CashCouponRuleConfigBiz;
import com.tqmall.mana.client.beans.cashcoupon.CreateRuleConfigResultDTO;
import com.tqmall.mana.client.beans.cashcoupon.GoodsConfigResultDTO;
import com.tqmall.mana.client.service.cashcoupon.RpcCashCouponRuleConfigService;
import com.tqmall.mana.component.util.BdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by zhanghong on 17/4/11.
 */
@Service
public class RpcCashCouponRuleConfigServiceImpl implements RpcCashCouponRuleConfigService {
    @Autowired
    private CashCouponRuleConfigBiz ruleConfigBiz;

    @Override
    public Result<CreateRuleConfigResultDTO> getCreateRuleConfigInfo(String cityCode) {
        if(StringUtils.isEmpty(cityCode)){
            return Result.wrapErrorResult("001","城市编码参数异常");
        }
        CashCouponRuleConfigBO couponRuleConfigBO=ruleConfigBiz.getCreateRuleConfigInfo(cityCode);
        if(couponRuleConfigBO==null){
            return Result.wrapSuccessfulResult(null);
        }
        CreateRuleConfigResultDTO configResultDTO = BdUtil.do2bo(couponRuleConfigBO,CreateRuleConfigResultDTO.class);
        List<GoodsConfigResultDTO> goodsConfigResultDTOs=BdUtil.do2bo4List(couponRuleConfigBO.getGoodsConfigBOList(),GoodsConfigResultDTO.class);
        configResultDTO.setGoodsConfigDTOList(goodsConfigResultDTOs);
        return Result.wrapSuccessfulResult(configResultDTO);
    }

    @Override
    public Result<Boolean> getIsOpenByCityCode(String cityCode, String source) {
        if(StringUtils.isEmpty(cityCode)){
            return Result.wrapErrorResult("001","城市编码参数错误");
        }
        CashCouponRuleConfigBO configBO = ruleConfigBiz.getConfigInfoByCityCode(cityCode);
        if(configBO == null){
            return Result.wrapSuccessfulResult(false);
        }

        return Result.wrapSuccessfulResult(true);
    }
}
