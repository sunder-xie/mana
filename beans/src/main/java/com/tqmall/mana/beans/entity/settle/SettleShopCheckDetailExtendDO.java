package com.tqmall.mana.beans.entity.settle;

import lombok.Data;

import java.util.Date;

@Data
public class SettleShopCheckDetailExtendDO {
    //主键ID
    private Integer id;

    //是否删除,Y删除,N未删除
    private String isDeleted;

    //创建时间
    private Date gmtCreate;

    //更新时间
    private Date gmtModified;

    //创建人
    private String creator;

    //修改人
    private String modifier;

    //settle_shop_check_detail主键id
    private Integer shopCheckDetailId;

    //奖励金状态 1:未生效 2:已生效
    private Integer rewardStatus;

    //奖励金生效时间
    private Date rewardEffectTime;

    //奖励金提现状态 1:未申请 2:申请中 3:审核通过 4:审核未通过 5:确认打款
    private Integer withdrawCashStatus;

    //申请人
    private String applyPeopleName;

    //申请时间
    private Date applyTime;

    //机滤补贴状态 1:未申请 2:已申请 3:已审核 4:已支付
    private Integer allowanceStatus;

    //订单仓库id
    private Integer warehouseId;

    //订单仓库名字
    private String warehouseName;

    //物料类型 1:机滤
    private Integer materialType;

    //物料数量
    private Integer materialNum;

}