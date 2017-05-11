package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jinju.zeng on 17/2/7.
 */
@Data
public class SettleCarOwnerInsuranceTypeDetailDTO implements Serializable {
    //保险名称id，冗余自insurance_form.insurance_type
    private Integer insuranceTypeId;

    //保费
    private BigDecimal insuranceFee;

    //车船税
    private BigDecimal taxFee;

    //保单号
    private String insuredFormNo;
}
