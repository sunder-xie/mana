package com.tqmall.mana.beans.VO.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jinju.zeng on 17/2/7.
 */
@Data
public class SettleCarOwnerInsuranceTypeDetailVO {
    //保险名称id，冗余自insurance_form.insurance_type
    private Integer insuranceTypeId;

    //保费
    private BigDecimal insuranceFee;

    //车船税
    private BigDecimal taxFee;

    //保单号
    private String insuredFormNo;

}
