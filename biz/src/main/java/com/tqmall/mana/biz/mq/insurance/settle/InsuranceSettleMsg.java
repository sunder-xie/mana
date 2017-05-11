package com.tqmall.mana.biz.mq.insurance.settle;

import lombok.Data;

/**
 * Created by huangzhangting on 17/2/8.
 */
@Data
public class InsuranceSettleMsg {
    /*
    * 消息类型
    * 1:保单基础信息 即 msgContent 对应的是 InsuranceSettleBasicMsg
    * 2:物料补贴信息 即 msgContent 对应的是 InsuranceSettleMaterialAllowanceMsg
    * */
    private Integer msgType;

    /* 消息内容，json字符串 */
    private String msgContent;

}
