package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleServiceCheckDetailDO {
    //主键ID
    private Integer id;

    //是否删除,Y删除,N未删除
    private String isDeleted;

    //创建时间
    private Date gmtCreate;

    //更新时间
    private Date gmtModified;

    //创建人
    private String creator;

    //修改人
    private String modifier;

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

    //服务包状态,3--待生效， 0--待发货, 1-配送中 2-已签收
    private Integer settlePackageStatus;

    //1期支付金额支付时间，冗余自insurance_virtual_form
    private Date gmtFirstPaid;

}