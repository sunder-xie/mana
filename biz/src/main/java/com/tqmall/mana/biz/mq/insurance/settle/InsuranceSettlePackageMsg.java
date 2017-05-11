package com.tqmall.mana.biz.mq.insurance.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 对应 insurance_user_service_package 的信息
 *
 * Created by huangzhangting on 17/1/20.
 */
@Data
public class InsuranceSettlePackageMsg {
    private String packageOrderSn; //服务包订单编号
    private String packageName; //服务包名称
    private BigDecimal packagePrice; //服务包市场价
    private BigDecimal packageFee; //服务包工时费

    /* 第一次支付信息 */
    private Integer firstPayId; //支付方式
    private String firstPayNo; //预付金支付流水号
    private BigDecimal firstPaidAmount; //预付金支付金额
    private Date gmtFirstPaid; //预付金支付时间

    /* 第二次支付信息 */
    private Integer secondPayId; //支付方式
    private String secondPayNo; //支付流水号
    private BigDecimal secondPaidAmount; //支付金额
    private Date gmtSecondPaid; //支付时间

}
