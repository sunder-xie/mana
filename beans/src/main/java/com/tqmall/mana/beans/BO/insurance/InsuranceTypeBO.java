package com.tqmall.mana.beans.BO.insurance;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhouheng on 16/12/24.
 */
@Data
public class InsuranceTypeBO {

    /**
     * 主键ID
     **/
    private Integer insuranceFormId;

    /**
     * 前端使用:
     */
    private String insuranceTypeDescription;

    /**
     * 保费,投保人投保的保费
     **/
    private BigDecimal insuredFee;

}
