package com.tqmall.mana.client.beans.settle;

import lombok.Data;

import java.util.Date;

/**
 *
 * 奖励金模式
 *
 * Created by huangzhangting on 17/2/16.
 */
@Data
public class ShopSettleDetailRewardModeDTO extends ShopSettleDetailDTO {
    private Integer rewardStatus; //奖励金状态 1:未生效 2:已生效
    private String rewardStatusName; //奖励金状态
    private Date rewardEffectTime; //奖励金生效时间
//    private String withdrawCashStatusName; //提现状态
//    private String applyPeopleName; //申请人
//    private Date applyTime; //申请时间
}
