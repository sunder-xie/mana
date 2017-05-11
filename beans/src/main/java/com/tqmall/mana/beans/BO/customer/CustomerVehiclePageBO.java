package com.tqmall.mana.beans.BO.customer;

import com.tqmall.mana.component.enums.CustomerSourceEnum;
import com.tqmall.mana.component.enums.InsureIntentionEnum;
import com.tqmall.mana.component.enums.InsureStatusEnum;
import lombok.Data;

import java.util.Date;

/**
 * Created by zhouheng on 16/12/26.
 */
@Data
public class CustomerVehiclePageBO {


    private Integer customerId;

    private Integer vehicleId;

    private String customerName;

    private String customerMobile;

    private String licencePlate;

    private Integer customerSource;
    /**
     * 前端展示:客户来源
     */
    private String customerSourceName;

    private Integer insureStatus;
    /**
     * 前端展示:保险状态
     */
    private String insureStatusName;

    private Date insureEndDate;

    private Integer insureIntention;
    /**
     * 前端使用:客户意向
     */
    private String insureIntentionName;

    public String getCustomerSourceName() {
        if (customerSourceName != null) {
            return customerSourceName;
        }
        return CustomerSourceEnum.codeDescription(this.customerSource);
    }

    public String getInsureStatusName() {
        if (insureStatusName != null) {
            return insureStatusName;
        }
        return InsureStatusEnum.codeDescription(insureStatus);
    }

    public String getInsureIntentionName() {
        if (insureIntentionName != null) {
            return insureIntentionName;
        }
        return InsureIntentionEnum.codeDescription(insureIntention);
    }

}
