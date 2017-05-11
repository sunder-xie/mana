package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleCarOwnerCheckDetailDO {
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

    //settle_service_check_detail 的唯一主键
    private String settleServiceSn;

    //真实保单唯一root，冗余自insurance_basic.insurance_order_sn
    private String insuranceOrderSn;

    //车主姓名,冗余自insurance_virtual_basic.insured_name
    private String vehiclePeopleName;

    //车牌号,冗余自insurance_virtual_basic.vehicle_sn
    private String vehicleSn;

    //行驶证
    private String vehicleLicense;

    //门店id，冗余自insurance_virtual_basic
    private Integer agentId;

    //门店名称，冗余自insurance_virtual_basic
    private String agentName;

    //门店帐号，根据agent_id获得uc的帐号
    private String agentAccount;

    //门店标签 1:云修店 2:档口店
    private Integer agentTag;

    //保险公司id
    private Integer insuranceCompanyId;

    //保险公司名称
    private String insuranceCompanyName;

    //保险模式
    private Integer cooperationModeId;

    //商业应交保费,冗余自insurance_virtual_form.virtual_insured_fee
    private BigDecimal syPayableInsuredFee;

    //交强应交保费,冗余自insurance_virtual_form.virtual_insured_fee
    private BigDecimal jqPayableInsuredFee;

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

    //车船税
    private BigDecimal jqTaxFee;

    //新车牌号
    private String newVehicleSn;

}