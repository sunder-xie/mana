package com.tqmall.mana.beans.BO.coupon;

import com.tqmall.mana.component.enums.CouponStatusEnum;
import lombok.Data;

/**
 * Created by huangzhangting on 16/12/8.
 */
@Data
public class CouponSendLogBO {

    private Integer id;

    private String sendContent;

    private Integer sendStatus;

    private String sendStatusStr;

    private String sendMobile;

    private Integer couponNum;

    private String couponTypeName;

    private String gmtCreateStr;

    private String templateDesc;

    public String getSendStatusStr() {
        if(sendStatusStr != null){
            return sendStatusStr;
        }
        return CouponStatusEnum.codeDescription(this.sendStatus);
    }
}
