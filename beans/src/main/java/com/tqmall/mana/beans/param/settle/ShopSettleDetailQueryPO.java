package com.tqmall.mana.beans.param.settle;

import com.tqmall.mana.beans.param.CommonPageQueryPO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huangzhangting on 17/1/20.
 */
@Data
public class ShopSettleDetailQueryPO extends CommonPageQueryPO implements Serializable{

    private Integer insuranceCompanyId; //保险公司id
    private Integer cooperationModeId; //保险模式
    private Integer shopId; //门店id
    private String insuredProvinceCode; //投保省份
    private String insuredCityCode; //投保城市
    private String insuredFormNo; //保单号
    private Integer insuranceTypeId; //险种
    private String vehicleSn; //车牌号

    private Date billSignTimeStart; //签单日期开始值
    private Date billSignTimeEnd; //签单日期结束值

    private Date insuredStartTimeStart; //起保日期开始值
    private Date insuredStartTimeEnd; //起保日期结束值

    private Integer settleFeeStatus; //结算状态 1:未结算 2:已结算

    private String packageOrderSn; //服务包物料订单编号

    private Integer settleRuleType; //对账类型：1:现金 2:奖励金 3:服务包物料

    private Integer confirmMoneyStatus; //淘汽确认收到保险公司款, 0:未收款 1:已收款

    private Integer rewardStatus; //奖励金状态 1-未生效 2-已生效
//    private Integer withdrawCashStatus; //奖励金提现状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
    private Date applyTimeStart; //申请日期开始值
    private Date applyTimeEnd; //申请日期结束值

    private Integer balanceStatus; //对账状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
    private Integer ifUseCoupon; //是否用券 0:否 1:是

    private Integer agentTag; //门店标签 1:云修店 2:档口店


    /** 目前只有erp在使用这个接口 */
    private Integer erpFlag = 1;

}
