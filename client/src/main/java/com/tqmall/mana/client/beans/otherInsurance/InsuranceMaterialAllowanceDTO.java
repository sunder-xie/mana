package com.tqmall.mana.client.beans.otherInsurance;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zwb on 17/2/21.
 */
@Setter
@Getter
public class InsuranceMaterialAllowanceDTO implements Serializable {
    /**主键ID**/
    private Integer id;

    /**创建时间**/
    private java.util.Date gmtCreate;

    /**创建人ID**/
    private Integer creator;

    /**更新时间**/
    private java.util.Date gmtModified;

    /**更新人ID**/
    private Integer modifier;

    /**是否删除,Y删除,N未删除**/
    private String isDeleted;

    /**门店手机号**/
    private String shopContact;

    /**订单号**/
    private String orderSn;

//    /**品牌车型**/
//    private String vehicleModel;

    /**物料类型 1:机滤**/
    private Integer materialType;

    /**物料数量**/
    private Integer materialNum;

    /**应补贴金额**/
    private java.math.BigDecimal payableAmount;

//    /**门店银行**/
//    private String shopBank;
//
//    /**门店银行账号**/
//    private String shopBankAccount;
//
//    /**收款人**/
//    private String payeeName;

    /**付款时间**/
    private java.util.Date gmtPaid;

    /**审核时间**/
    private java.util.Date gmtAudit;

    /**淘汽内部保单号，可唯一识别投保单、保单**/
    private String tqInsuranceSn;

    /**根据服务包价格档位进行返现**/
    private Integer packageLevel;

    /**申请时间**/
    private Integer gmtApplied;

    /**服务包生效时间**/
    private java.util.Date gmtPackageValid;

    /**0:未申请 1:已申请 2:已审核 3:已支付**/
    private Integer allowanceStatus;

    /**订单仓库名字**/
    private String whsName;

    /**订单仓库id**/
    private Integer whsId;

    /**请款申请时间**/
    private java.util.Date gmtApplid;

    /**请款申请通过时间**/
    private java.util.Date gmtApplidApprove;

    /**申请状态修改人**/
    private Integer applidModifier;

    /**审核状态修改人**/
    private Integer auditModifier;

    /**门店名称**/
    private String shopName;

    /**门店id**/
    private Integer shopId;

    //门店标签名称
    private String agentTagName;


    /**请款状态显示调用**/
    public String getAllowanceStatusDescription(Integer allowanceStatus){
        if(allowanceStatus==0){
            return "未申请";
        }
        if(allowanceStatus==1){
            return "已申请";
        }
        if(allowanceStatus==2){
            return "已审核";
        }
        if(allowanceStatus==3){
            return "已付款";
        }
        return null;
    }
}
