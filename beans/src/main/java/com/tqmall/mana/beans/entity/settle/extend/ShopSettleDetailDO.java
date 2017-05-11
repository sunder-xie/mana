package com.tqmall.mana.beans.entity.settle.extend;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/1/20.
 */
@Data
public class ShopSettleDetailDO{
    /** settle_shop_check_detail 表字段 */
    private Integer id; // settle_shop_check_detail表主键id
    private String insuranceOrderSn; //淘汽保单号
    private String cooperationModeName; //保险模式
    private String insuranceCompanyName; //保险公司名称

    private BigDecimal settleRate; //结算比例
    private BigDecimal settleFee; //结算金额

    private Integer settleFeeStatus; //结算状态, 0-未审核 1-未支付 2-已支付
    private String auditPeopleName; //审核人员
    private Date auditTime; //审核时间
    private String settlePeopleName; //结算人员（确认支付）
    private Date settleTime; //结算时间

    private Integer settleProjectId; //结算项目id

    private String vehicleSn; //车牌号
    private String newVehicleSn; //新车牌号

    private Integer shopId; //门店id
    private String shopName; //门店名称
    private String shopAccount; //门店帐号
    private Integer agentTag; //门店标签 1:云修店 2:档口店

    //是否用券 0:否 1:是
    private Integer ifUseCoupon;
    //用券数量
    private Integer couponNumber;
    //券金额
    private BigDecimal couponAmount;
    //结算基数
    private BigDecimal settleBaseAmount;

    //对账状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
    private Integer balanceStatus;
    //申请人
    private String applyPeopleName;
    //申请时间
    private Date applyTime;


    /** settle_form 表字段 */
    private Integer insuranceFormId; //insurance_form表主键id
    private String insuredFormNo; //保单号
    private String insuredProvince; //投保省
    private String insuredCity; //投保市
    private Integer insuranceTypeId; //险种
    private BigDecimal insuredFee; //保费

    private Date billSignTime; //签单日期
    private Date insuredStartTime; //起保日期

    private String applicantName; //投保人名称
    private String beAppliedName; //被保人名称

    private BigDecimal taxFee; //保单税费


    /** settle_service_check_detail 表字段 */
    private String settlePackageOrderSn; //服务包订单编号
    private String settlePackageName; //服务包名称
    private BigDecimal settlePackagePrice; //服务包价值
    private BigDecimal settlePackageFee; //服务包工时费
    private Date gmtFirstPaid; //1期支付时间


    /** settle_company_check_detail 表字段 */
    private Integer confirmMoneyStatus; //淘汽确认收到保险公司服务款状态 0:未收款 1:已收款
    private Date confirmMoneyTime; //淘汽确认收到保险公司服务款的时间
    private String confirmMoneyPeopleName; //收款人员


    /** settle_shop_check_detail_extend 表字段 */
    private Integer rewardStatus; //奖励金状态 1:未生效 2:已生效
    private Date rewardEffectTime; //奖励金生效时间
//    private Integer withdrawCashStatus; //奖励金提现状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款

}
