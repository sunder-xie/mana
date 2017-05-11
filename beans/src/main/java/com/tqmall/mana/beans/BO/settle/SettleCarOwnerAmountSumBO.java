package com.tqmall.mana.beans.BO.settle;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zengjinju on 17/1/22.
 */
@Data
public class SettleCarOwnerAmountSumBO {

    //1期收款合计
    private BigDecimal firstPayTotalAmount;

    //2期收款合计
    private BigDecimal secondPayTotalAmount;

    //应商业险保费总额
    private BigDecimal syTotalPayableInsuredFee;

    //交强险保费总额
    private BigDecimal jqTotalPayableInsuredFee;

    private BigDecimal payableTotalAmount;

    //交强险税费总额
    private BigDecimal totalJqTaxFee;

}
