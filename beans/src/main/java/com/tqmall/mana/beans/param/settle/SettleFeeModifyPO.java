package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/3/10.
 */
@Data
public class SettleFeeModifyPO {
    private String insuranceOrderSn; //淘汽保单号
    private String couponSn; //现金券编号
    private BigDecimal couponAmount; //现金券金额
    private Date couponCashTime; //现金券兑现时间

    //撤销使用现金券时用到
    private BigDecimal bizInsuranceRate; //商业险比例
    private BigDecimal forceInsuranceRate; //交强险比例

}
