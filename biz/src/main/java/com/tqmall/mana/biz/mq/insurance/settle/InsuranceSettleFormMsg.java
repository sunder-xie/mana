package com.tqmall.mana.biz.mq.insurance.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * 对应 insurance_form 的信息
 *
 * Created by huangzhangting on 17/1/20.
 */
@Data
public class InsuranceSettleFormMsg {
    private Integer id; //insurance_form表主键id
    private String insuredApplyNo; //投保单号
    private String insuredFormNo; //保单号
    private Integer insuranceTypeId; //保险类别
    private Integer cooperationModeId; //和淘气合作模式
    private Date billSignTime; //保单缴费完成日期
    private Date insuredStartTime; //起保日期
    private BigDecimal insuredFee; //保费
    private BigDecimal tax;//保单税费

    //险种条目
    List<InsuranceSettleItemMsg> itemMsgList;

}
