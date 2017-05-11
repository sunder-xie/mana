package com.tqmall.mana.beans.entity.settle.extend;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by huangzhangting on 17/2/16.
 */
@Data
public class ShopSettleDetailExtendDO {
    /** settle_shop_check_detail_extend 表字段 */
    //主键ID
    private Integer id;

    //settle_shop_check_detail主键id
    private Integer shopCheckDetailId;

    //奖励金提现状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
    private Integer withdrawCashStatus;


    /** settle_shop_check_detail 表字段 */
    //各结算项目的业务唯一主键
    private String bizSn;

    //门店id，冗余自insurance_basic
    private Integer agentId;

    //结算金额
    private BigDecimal settleFee;

    //结算状态, 0-未审核 1-未支付 2-已支付
    private Integer settleFeeStatus;

    //保险模式
    private Integer cooperationModeId;

}
