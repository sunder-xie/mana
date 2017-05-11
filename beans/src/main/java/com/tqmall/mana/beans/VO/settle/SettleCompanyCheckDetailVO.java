package com.tqmall.mana.beans.VO.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zengjinju on 17/1/21.
 */
@Data
public class SettleCompanyCheckDetailVO {
    //settle_company_check_detail主键id
    private Integer id;

    //此表的唯一主键，保险公司保单号-缴费成功，冗余自insurance_form.outer_insurance_form_no
    private String insuredFormNo;

    //insurance_form 的唯一主键id，冗余用于校对数据
    private Integer insuranceFormId;

    //真实保单唯一root，冗余自insurance_basic.insurance_order_sn
    private String insuranceOrderSn;

    //保险名称id，冗余自insurance_form.insurance_type
    private Integer insuranceTypeId;

    //保费,冗余自insurance_form.insurance_fee
    private BigDecimal insuredFee;

    //模式，例如模式一：买保险送服务包
    private Integer cooperationModeId;

    //签单日期，模式1+2--insurance_form.pay_time,模式三--即settle_company_check_detail的confirm_money_time
    private Date billSignTime;

    //投保人名称,冗余自insurance_basic.applicant_name
    private String applicantName;

    //被保人名称,冗余自insurance_basic.insured_name
    private String beAppliedName;

    //车牌号,冗余自insurance_basic.vehicle_sn
    private String vehicleSn;

    //门店id，冗余自insurance_basic
    private Integer agentId;

    //门店名称，冗余自insurance_basic
    private String agentName;

    //门店帐号，根据agent_id获得uc的帐号
    private String agentAccount;

    //门店标签名称
    private String agentTagName;

    //投保单号:安心保费计算时候返回,冗余自insurance_form.outer_insurance_apply_no
    private String insuredApplyNo;

    //起保日期，冗余自insurance_form.package_start_time
    private Date insuredStartTime;

    //投保所在省，冗余自insurance_basic
    private String insuredProvince;

    //投保所在市，冗余自insurance_basic
    private String insuredCity;

    //投保所在省编码，来自insurance_region
    private String insuredProvinceCode;

    //投保所在城市编码，来自insurance_region
    private String insuredCityCode;

    //保险公司id
    private Integer insuranceCompanyId;

    //保险公司名称
    private String insuranceCompanyName;

    //购买保险场景id 1:商业险 2:商业交强同保 3:交强险 4:第三方责任交强同保 5:责任险单保
    private Integer scenarioTypeId;

    private String scenarioTypeName;

    //保费分成比例
    private BigDecimal insuredRoyaltyRate;

    //保费分成，保险公司给淘汽
    private BigDecimal insuredRoyaltyFee;

    //淘汽确认收到保险公司款, 0:未收款 1:已收款
    private Integer confirmMoneyStatus;

    //确认收款时间
    private Date confirmMoneyTime;

    //收款人员
    private String confirmMoneyPeopleName;

    //车船税
    private BigDecimal taxFee;

}
