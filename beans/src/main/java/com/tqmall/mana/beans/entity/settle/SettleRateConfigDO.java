package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SettleRateConfigDO {
    //主键ID
    private Integer id;

    //是否删除,Y删除,N未删除
    private String isDeleted;

    //创建时间
    private Date gmtCreate;

    //更新时间
    private Date gmtModified;

    //创建人
    private String creator;

    //修改人
    private String modifier;

    //比例key
    private String rateKey;

    //比例值
    private BigDecimal rateValue;

    //比例说明
    private String rateExplain;

    //比例类型 1:交强险比例 2:商业险比例
    private Integer rateType;

    //保险公司id
    private Integer insuranceCompanyId;
    //保险公司名称
    private String insuranceCompanyName;

    //适用范围 0:保险公司通用 1:地区 2:门店
    private Integer applyRange;

}