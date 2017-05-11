package com.tqmall.mana.client.beans.param;

import lombok.Data;

/**
 *
 * 服务包模式，门店对账数据查询参数
 *
 * Created by huangzhangting on 17/2/16.
 */
@Data
public class ShopSettleDetailPackageModeQueryParam extends ShopSettleDetailQueryParam {
    private Integer cooperationModeId; //保险模式（枚举）
    private String packageOrderSn; //服务包物料订单编号

//    private Integer settleFeeStatus; //结算状态(支付状态), 0-未审核 1-未支付 2-已支付

    private Integer confirmMoneyStatus; //淘汽确认收到保险公司款, 0:未收款 1:已收款

}
