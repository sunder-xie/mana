package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 结算金额修改参数
 *
 * Created by huangzhangting on 17/1/21.
 */
@Data
public class SettleFeeModifyParam implements Serializable{
    private String insuranceOrderSn; //淘汽保单号
    private String couponSn; //现金券编号
    private BigDecimal couponAmount; //现金券金额
    private Date couponCashTime; //现金券兑现时间

}
