package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zengjinju on 17/1/20.
 */
@Data
public class SettleCarOwnerCheckerDetailDTO implements Serializable {

    private Integer id;

    //settle_service_check_detail 的唯一主键(服务包订单号)
    private String settleServiceSn;

    //真实保单唯一root，冗余自insurance_basic.insurance_order_sn
    private String insuranceOrderSn;

    //车主姓名,冗余自insurance_virtual_basic.insured_name
    private String vehiclePeopleName;

    //车牌号,冗余自insurance_virtual_basic.vehicle_sn
    private String vehicleSn;
    //新车牌号
    private String newVehicleSn;

    //行驶证
    private String vehicleLicense;

    //门店id，冗余自insurance_virtual_basic
    private Integer agentId;

    //门店名称，冗余自insurance_virtual_basic
    private String agentName;

    //门店账号
    private String agentAccount;

    //门店标签名称
    private String agentTagName;

    //保险公司id
    private Integer insuranceCompanyId;

    //保险公司名称
    private String insuranceCompanyName;

    //支付方式,冗余自insurance_virtual_form
    private Integer firstPayId;

    //1期支付金额,冗余自insurance_virtual_form
    private BigDecimal firstPaidAmount;

    //1期支付金额支付时间，冗余自insurance_virtual_form
    private Date gmtFirstPaid;

    //1期支付金额支付流水号，冗余自insurance_virtual_form
    private String firstPayNo;

    //1期支付状态，0-未支付 1- 已支付
    private Integer firstPayStatus;

    //尾款支付方式,取自账户账务fc_payment.pay_id
    private Integer secondPayId;

    //尾款支付金额,计算方式：max(服务包价值,计算的投保金额)-1期支付金额
    private BigDecimal secondPaidAmount;

    //尾款支付时间
    private Date gmtSecondPaid;

    //尾款支付流水号
    private String secondPayNo;

    //尾款支付状态，0-未支付 1- 已支付
    private Integer secondPayStatus;

    //淘汽是否收到所有用户打款 。 0-未收到 1- 已收到
    private Integer isTqmallGetPaid;

    //确认收到打款操作人员
    private String tqmallGetPaidPeopleName;

    //确认收到打款操作时间
    private Date tqmallGetPaidTime;

    //淘汽支付到保险公司状态, 0-未审核 1-未支付 2-已支付
    private Integer isTqmallPayStatus;

    //审核人员
    private String auditPeopleName;

    //审核时间
    private Date auditTime;

    //服务包名称
    private String settlePackageName;

    //服务包价值
    private BigDecimal settlePackagePrice;

    //保险模式
    private Integer cooperationModeId;

    private List<SettleCarOwnerInsuranceTypeDetailDTO> insuranceTypeDetailDTOList = new ArrayList<SettleCarOwnerInsuranceTypeDetailDTO>();
}
