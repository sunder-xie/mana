package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 结算参数
 *
 * Created by huangzhangting on 17/1/21.
 */
@Data
public class SettleParam implements Serializable{
    private List<Integer> idList; //数据对象id集合
    private String operator; //操作人员名称
}
