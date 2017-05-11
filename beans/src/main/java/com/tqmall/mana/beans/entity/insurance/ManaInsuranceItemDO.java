package com.tqmall.mana.beans.entity.insurance;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ManaInsuranceItemDO {
    public static final Integer INSURANCE_TYPE_SY = 2;//商业险

    public static final String HAVE_REMARKS = "";
    public static final String NO_HAVE_REMARKS = "无此险种";
    public static final String NO_DO_REMARKS = "未去处理数据";


    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    /*====基本====*/
    private Integer insuranceBasicId; //保单项目id,关联insurance_basic.id

    private Integer insuranceType; //保险类别:1表示交强险,2表示商业险

    private Integer isDeductible; //是否不计免赔:0表示不记免赔,1表示记录免赔,2表示没有免赔选项

    private String insuranceName; //险别名称

    private BigDecimal insuranceAmount; //保险额度

    private BigDecimal insuranceFee; //保费(安心)

    /*=== 各个网站处理后得到=====*/
    private BigDecimal insuranceFeeRenBao; //保费(人保)

    private String renBaoRemark; //人保备注

    private BigDecimal insuranceFeePingAn; //保费(平安)

    private String pingAnRemark; //平安备注

}