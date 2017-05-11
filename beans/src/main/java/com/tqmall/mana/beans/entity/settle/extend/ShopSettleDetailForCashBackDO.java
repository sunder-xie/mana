package com.tqmall.mana.beans.entity.settle.extend;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/2/7.
 */
@Data
public class ShopSettleDetailForCashBackDO {
    //主键ID
    private Integer id;

    //门店id，冗余自insurance_basic
    private Integer agentId;

    //结算金额
    private BigDecimal settleFee;
    
    //此表的唯一主键，保险公司保单号-缴费成功，冗余自insurance_form.outer_insurance_form_no
    private String insuredFormNo;

    //保险名称id，冗余自insurance_form.insurance_type
    private Integer insuranceTypeId;

    //保费,冗余自insurance_form.insurance_fee
    private BigDecimal insuredFee;

    //结算状态, 0-未审核 1-未支付 2-已支付
    private Integer settleFeeStatus;

    //保险公司id
    private Integer insuranceCompanyId;
}
