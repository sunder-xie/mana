package com.tqmall.mana.beans.param.settle;

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
public class ServicePackageModifyPO implements Serializable{
    private List<String> orderSnList; //物料订单编号集合
    private Integer packageStatus; //服务包状态
    private String operator; //操作人员名称
}
