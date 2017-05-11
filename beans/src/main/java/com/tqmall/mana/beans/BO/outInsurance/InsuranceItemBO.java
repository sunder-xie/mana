package com.tqmall.mana.beans.BO.outInsurance;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by zxg on 16/12/2.
 * 19:26
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Setter
@Getter
public class InsuranceItemBO {

    private Integer deductible; //是否不计免赔:0表示不记免赔,1表示记录免赔,2表示没有免赔选项

    private String insuranceName; //险别名称

    private BigDecimal insuranceAmount; //保险额度

    private BigDecimal insuranceFee; //保费(安心)
    /**
     * 前端使用
     */
    private String deductiveName;

}
