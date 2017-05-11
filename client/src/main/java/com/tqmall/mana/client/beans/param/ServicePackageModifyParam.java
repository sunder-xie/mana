package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 服务包修改参数
 *
 * Created by huangzhangting on 17/1/21.
 */
@Data
public class ServicePackageModifyParam implements Serializable{
    private List<String> orderSnList; //物料订单编号集合
    private Integer packageStatus; //服务包状态 1-配送中 2-已签收
    private String operator; //操作人员名称
}
