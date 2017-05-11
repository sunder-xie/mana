package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleServiceCheckDetailDTO implements Serializable {
    //主键ID
    private Integer id;

    //真实保单唯一root，冗余自insurance_basic.insurance_order_sn
    private String insuranceOrderSn;

    //服务包订单编号，此表唯一的业务主键
    private String settlePackageOrderSn;

    //服务包名称
    private String settlePackageName;

    //服务包价值，冗余insurance_service_package.market_price
    private BigDecimal settlePackagePrice;

    //服务包工时费
    private BigDecimal settlePackageFee;

    //服务包状态, 3--待生效，0--待发货, 1-配送中 2-已签收
    private Integer settlePackageStatus;
}