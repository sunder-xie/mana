package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huangzhangting on 17/1/20.
 */
@Data
public class ShopSettleDetailQueryParam implements Serializable{
    private Integer pageIndex; //页码，从 1 开始
    private Integer pageSize; //每页条数

    private Integer insuranceCompanyId; //保险公司id
    private Integer shopId; //门店id
    private String insuredProvinceCode; //投保省份编码
    private String insuredCityCode; //投保城市编码
    private String insuredFormNo; //保单号
    private Integer insuranceTypeId; //险种（枚举）
    private String vehicleSn; //车牌号

    private Date billSignTimeStart; //签单日期开始值
    private Date billSignTimeEnd; //签单日期结束值

    private Date insuredStartTimeStart; //起保日期开始值
    private Date insuredStartTimeEnd; //起保日期结束值

    private Integer balanceStatus; //对账状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
    private Integer ifUseCoupon; //是否用券 0:否 1:是

    private Date applyTimeStart; //申请时间开始值
    private Date applyTimeEnd; //申请时间结束值

    private Integer agentTag; //门店标签 1:云修店 2:档口店
}
