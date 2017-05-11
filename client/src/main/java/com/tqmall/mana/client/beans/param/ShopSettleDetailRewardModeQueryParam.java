package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.util.Date;

/**
 *
 * 奖励金模式，门店对账数据查询参数
 *
 * Created by huangzhangting on 17/2/16.
 */
@Data
public class ShopSettleDetailRewardModeQueryParam extends ShopSettleDetailQueryParam {
    private Integer rewardStatus; //奖励金状态 1-未生效 2-已生效
//    private Integer withdrawCashStatus; //奖励金提现状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
//    private Date applyTimeStart; //申请日期开始值
//    private Date applyTimeEnd; //申请日期结束值

}
