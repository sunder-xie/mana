package com.tqmall.mana.biz.mq.insurance.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 对应 insurance_item 信息
 *
 * Created by zengjinju on 17/1/20.
 */
@Data
public class InsuranceSettleItemMsg {
    /**
     * 保单项目id, 关联insurance_form.id
     **/
    private Integer insuranceFormId;
    /**
     * 保险类别:1表示交强险,2表示商业险
     **/
    private Integer insuranceType;
    /**
     * 险别代码，表insurance_category的相应字段
     **/
    private String insuranceCategoryCode;
    /**
     * 险别名称
     **/
    private String insuranceName;
    /**
     * 保费
     **/
    private BigDecimal insuranceFee;
}
