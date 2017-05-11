package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 奖励金修改参数
 *
 * Created by huangzhangting on 17/1/21.
 */
@Data
public class RewardModifyParam implements Serializable{
    private List<Integer> idList; //数据id集合
    private Integer withdrawCashStatus; //奖励金提现状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
    private String operator; //操作人员名称
}
