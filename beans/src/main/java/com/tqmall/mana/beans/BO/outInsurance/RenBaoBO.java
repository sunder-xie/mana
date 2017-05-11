package com.tqmall.mana.beans.BO.outInsurance;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 16/11/30.
 * 10:12
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Setter
@Getter
public class RenBaoBO {

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
    private String carDisplay; //--not used
    private String carGear; //--not used
    private String carType;
    private String carSeatNum;
    private String carYear;

    //10.保险种类名称和投保金额
    /**
     * amountName:保险种类名称
     * amountDeductible：是否不计免赔:0表示不记免赔,1表示记录免赔,2表示没有免赔选项
     * amountPrice:投保金额,单位元
     */
    List<InsuranceItemBO> amountList;


    /* 代码中获得生成的*/
    private String sessionId; // 贯穿全文的 sessionId
    private String provinceCode; // 省份编码
    private String cityCode; // 城市编码
    private String birthday;//生日，例如1976/02/11

    private String startDateStr;//人保开始时间
    private String endDateStr;//人保结束时间


    // 随机生成 手机号和邮箱
    private String mobilePhone;
    private String email;
}
