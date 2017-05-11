package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/3/23.
 */
@Data
public class UseCouponParam implements Serializable {
    private Integer insuranceCompanyId; //保险公司id
    private String cityCode; //投保城市编码

    private String insuranceOrderSn; //淘汽保单号
    private String couponSn; //现金券编号
    private BigDecimal couponAmount; //现金券金额
    private Date couponCashTime; //现金券兑现时间

    //公式key
    private String bizInsuranceFormulaKey; // 商业险返利计算公式key
    private String forceInsuranceFormulaKey; // 交强险返利计算公式key

}
