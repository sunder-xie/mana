package com.tqmall.mana.beans.param.settle;

import lombok.Data;

import java.util.Date;

/**
 * Created by zengjinju on 17/1/21.
 */
@Data
public class SettleCompanyCheckDetailBizParam {
    private Integer id;

    private Integer page = 1;
    private Integer pageSize = 20;
    //门店名称
    private String agentName;
    //保险模式
    private Integer cooperationModeId;
    //签单开始日期
    private Date billSignTimeStart;
    //签单结束日期
    private Date billSignTimeEnd;
    //保单号
    private String insuredFormNo;
    //服务费确实收款状态
    private Integer confirmMoneyStatus;
    //起保日期开始时间
    private Date insuredStartTimeStart;
    //起保日期结束时间
    private Date insuredStartTimeEnd;
    //投保所在省
    private String insuredProvinceCode;
    //投保所在市
    private String insuredCityCode;
    //投保场景
    private Integer scenarioTypeId;
    //服务费确认收款开始时间
    private Date confirmMoneyTimeStart;
    //服务费确认收款结束时间
    private Date comfirmMoneyTimeEnd;
    //险种(1.交强险，2.商业险)
    private Integer insuranceTypeId;
    //车牌号
    private String vehicleSn;
    //保险公司id
    private Integer insuranceCompanyId;
    //门店标签 1:云修店 2:档口店
    private Integer agentTag;
}
