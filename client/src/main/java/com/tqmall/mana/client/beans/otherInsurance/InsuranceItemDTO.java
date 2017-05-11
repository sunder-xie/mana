package com.tqmall.mana.client.beans.otherInsurance;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zxg on 16/12/2.
 * 19:26
 * no bug,以后改代码的哥们，祝你好运~！！
 * 安心的每个保险子项的项目类
 */
@Setter
@Getter
public class InsuranceItemDTO implements Serializable{
    // 显示手写，保证代码的健壮性，此时为 1.0 版本
    private static final long serialVersionUID = 12021910L;//12月02日19点的1.0版本

    private Integer deductible; //是否不计免赔:0表示不记免赔,1表示记录免赔,2表示没有免赔选项

    private String insuranceName; //险别名称

    private BigDecimal insuranceAmount; //保险额度

    private BigDecimal insuranceFee; //保费(安心)
}
