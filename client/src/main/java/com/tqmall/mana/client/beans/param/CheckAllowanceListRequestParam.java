package com.tqmall.mana.client.beans.param;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zwb on 17/2/20.
 */
@Data
public class CheckAllowanceListRequestParam implements Serializable{
    private static final long serialVersionUID = -8179784993977348348L;

    /**服务包生效起始时间**/
    private java.util.Date gmtPackageValidStart;

    /**服务包生效截止时间**/
    private java.util.Date gmtPackageValidEnd;

    /**门店名称**/
    private String shopName;

    /**订单仓库名字**/
    private String whsName;

    /**订单号**/
    private String orderSn;

    /**请款状态**/
    private Integer allowanceStatus;

//    /**门店银行**/
//    private String shopBank;
//
//    /**收款人**/
//    private String payeeName;

    //门店标签 1:云修店 2:档口店
    private Integer agentTag;


    private Integer pageNum = 1;//页码

    private Integer pageSize = 15;//每页数量(默认15条)
}
