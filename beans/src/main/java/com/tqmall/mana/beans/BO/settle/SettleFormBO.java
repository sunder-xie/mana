package com.tqmall.mana.beans.BO.settle;

import com.tqmall.mana.beans.entity.settle.SettleFormDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zengjinju on 17/1/22.
 */
@Data
public class SettleFormBO extends SettleFormDO {
    //保险公司id
    private Integer insuranceCompanyId;

    //保险公司名称
    private String insuranceCompanyName;

    //购买保险场景id 1:商业险 2:商业交强同保 3:交强险 4:第三方责任交强同保 5:责任险单保
    private Integer scenarioTypeId;

    //保费分成比例
    private BigDecimal insuredRoyaltyRate;

    //保费分成，保险公司给淘汽
    private BigDecimal insuredRoyaltyFee;

    //淘汽确认收到保险公司款, 0:未收款 1:已收款
    private Integer confirmMoneyStatus;

    //确认收款时间
    private Date confirmMoneyTime;

    //收款人员
    private String confirmMoneyPeopleName;
}
