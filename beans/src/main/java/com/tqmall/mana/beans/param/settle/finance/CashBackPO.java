package com.tqmall.mana.beans.param.settle.finance;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * erp确认收到保险公司返利后，通知finance奖励金生效参数
 *
 * Created by huangzhangting on 17/2/7.
 */
@Data
public class CashBackPO {
    private String insuranceNo;
    private Integer insuranceType;
    private BigDecimal charges;  //服务费（保费）
    private Integer shopId;
    private BigDecimal reward;  //奖励金
    private Integer cooperationMode;
}
