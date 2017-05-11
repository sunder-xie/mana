package com.tqmall.mana.beans.BO.settle.calculate;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huangzhangting on 17/3/23.
 */
@Data
public class InsuranceSettleFormBO {
    private Integer id; //insurance_form表主键id
//    private String insuredApplyNo; //投保单号
    private String insuredFormNo; //保单号
    private Integer insuranceTypeId; //保险类别
//    private Integer cooperationModeId; //和淘气合作模式
    private Date billSignTime; //签单日期
    private Date insuredStartTime; //起保日期
    private BigDecimal insuredFee; //保费
    private BigDecimal tax;//保单税费
}
