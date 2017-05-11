package com.tqmall.mana.client.beans.cashcoupon;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhanghong on 17/4/11.
 * 生成规则配置DTO
 */
@Data
public class CreateRuleConfigResultDTO implements Serializable {
    /**线上交强险最低保费*/
    private BigDecimal onlineForcibleMinfee;
    /**线上商业险最低保费*/
    private BigDecimal onlineCommercialMinfee;
    /**线上保单缴费有效期限(单位:天)*/
    private Integer onlineFormValidateDays;
    /**线上业务模式 1:奖励金*/
    private Integer onlineCooperationMode;
    /**线下交强险最低保费*/
    private BigDecimal offlineForcibleMinfee;
    /**线下商业险最低保费*/
    private BigDecimal offlineCommercialMinfee;
    /**线下保单缴费有效期限(单位:天)*/
    private Integer offlineFormValidateDays;
    /**线下业务模式 1:奖励金*/
    private Integer offlineCooperationMode;
    /**现金券券面值**/
    private BigDecimal faceAmount;

    /**有效期限(单位:天)**/
    private Integer cashCouponValidateDays;

    /**券的生成状态:0关闭 1:开启**/
    private Integer ruleStatus;
    /**用券后交强险返点比例*/
    private BigDecimal forcibleRebateRatio;
    /**用券后商业险返点比例*/
    private BigDecimal commercialRebateRatio;

    /**配置列表*/
    private List<GoodsConfigResultDTO> goodsConfigDTOList;

}
