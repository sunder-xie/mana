package com.tqmall.mana.beans.entity.customer;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

@Data
public class ManaCustomerCommunicateRecordDO {
    private Integer id;

    private Date gmtCreate;

    private Date gmtModified;

    private String creator;

    private String modifier;

    private Integer customerVehicleId;

    private String customerMobile;

    private Integer communicateChannel;

    private Date communicateDate;

    private String communicateContent;

    private Integer recommendShopId;

    private String recommendShopInfo;

    private String remarkInfo;

    public boolean isEmpty() {
        if (communicateChannel != null) {
            return false;
        }
        if (communicateDate != null) {
            return false;
        }
        if (!StringUtils.isEmpty(communicateContent)) {
            return false;
        }
        if (recommendShopId != null) {
            return false;
        }
        return true;
    }
}