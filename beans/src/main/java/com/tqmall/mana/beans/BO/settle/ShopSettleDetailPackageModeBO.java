package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/1/20.
 */
@Data
public class ShopSettleDetailPackageModeBO extends ShopSettleDetailBO {

    private BigDecimal settlePackageFee; //服务包工时费
    private String settlePackageName; //服务包名称
    private BigDecimal settlePackagePrice; //服务包价值
    private Date gmtFirstPaid; //1期支付时间
    private String settlePackageOrderSn; //服务包订单编号

//    private String settleStatusName; //结算状态


    /** 统计数据 */
    private BigDecimal totalServiceFee; //服务包工时费合计
    private BigDecimal totalCash; //应返现金合计

}
