package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/1/20.
 */
@Data
public class ShopSettleDetailDTO implements Serializable{
    private Integer id; // settle_shop_check_detail表主键id
    private String insuranceOrderSn; //淘汽保单号
    private String cooperationModeName; //保险模式

    private String insuranceCompanyName; //保险公司名称

    private BigDecimal settleRate; //结算比例
    private BigDecimal settleFee; //结算金额

    private String vehicleSn; //车牌号
    private String newVehicleSn; //新车牌号

    private Integer shopId; //门店id
    private String shopName; //门店名称
    private String shopAccount; //门店帐号
//    private String shopType; //门店类型

    private String auditPeopleName; //审核人员
    private Date auditTime; //审核时间

    private String settlePeopleName; //结算人员（确认支付人员/确认打款员）
    private Date settleTime; //结算时间

    private Integer insuranceFormId; //insurance_form表主键id
    private String insuredFormNo; //保单号
    private String insuredProvince; //投保省
    private String insuredCity; //投保市
    private String insuranceType; //险种
    private BigDecimal insuredFee; //保费

    private Date billSignTime; //签单日期
    private Date insuredStartTime; //起保日期

    private String applicantName; //投保人名称
    private String beAppliedName; //被保人名称

    private BigDecimal taxFee; //保单税费

    private String confirmMoneyStatusName; //淘汽确认收到保险公司服务费状态
    private String confirmMoneyPeopleName; //服务费确认收款人员
    private Date confirmMoneyTime; //服务费确认收款时间

    //是否用券 0:否 1:是
    private Integer ifUseCoupon;
    //用券数量
    private Integer couponNumber;
    //券金额
    private BigDecimal couponAmount;
    //结算基数
    private BigDecimal settleBaseAmount;
    //对账状态
    private String balanceStatusName;
    //申请人
    private String applyPeopleName;
    //申请时间
    private Date applyTime;

    //券号（目前就一张券，先这么处理）
    private String couponSn;

    //门店标签名称
    private String agentTagName;


    /** 统计数据 */
    private BigDecimal totalInsuredFee; //保费合计
    private BigDecimal totalSettleFee; //结算金额合计
    private BigDecimal totalCouponAmount; //券金额合计

}
