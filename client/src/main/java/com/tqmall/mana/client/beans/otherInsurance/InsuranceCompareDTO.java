package com.tqmall.mana.client.beans.otherInsurance;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 16/11/30.
 * 10:12
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Setter
@Getter
public class InsuranceCompareDTO implements Serializable{
    // 显示手写，保证代码的健壮性，此时为 1.0 版本
    private static final long serialVersionUID = 11301010L;//11月30日10点的1.0版本

    //保单项目id,关联insurance_basic.id
    private Integer insuranceBasicId;

    // 投保省
    private String provinceName;
    //投保市
    private String cityName;
    //车牌
    private String licenseNo;
    //车辆登记日期
    private Date registerDate;

    //5.vin码
    private String vinCode;
    //6.发动机码
    private String engineNo;

    //7.车主姓名
    private String peopleName;
    //8.身份证
    private String peopleCard;

    //9.车的品牌型号----（编码、排量、档位、什么类型、座位数、年款）
    private String carBrandCode;
    private String carDisplay;
    private String carGear;
    private String carType;
    private String carSeatNum;
    private String carYear;

    // 10.保险种类名称和投保金额
    /**
     * insuranceName:保险种类名称
     * deductible：是否不计免赔:0表示不记免赔,1表示记录免赔,2表示没有免赔选项
     * insuranceAmount:投保金额,单位元
     */
    List<InsuranceItemDTO> amountList;


}
