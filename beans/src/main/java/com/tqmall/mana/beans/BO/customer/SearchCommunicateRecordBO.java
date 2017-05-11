package com.tqmall.mana.beans.BO.customer;

import com.tqmall.mana.component.enums.CommunicateChannelEnum;
import lombok.Data;

import java.util.Date;

/**
 * Created by zhouheng on 16/12/16.
 */
@Data
public class SearchCommunicateRecordBO {

    private Integer id;

    private String creator;

    private Integer customerVehicleId;

    private Integer communicateChannel;

    //沟通渠道 0:车主致电 1:电话回访 2:拜访
    private String communicateChannelName;

    private Date communicateDate;

    private String communicateContent;

    private Integer recommendShopId;

    private String recommendShopInfo;

    private String customerMobile;

    public String getCommunicateChannelName() {
        if (communicateChannelName != null) {
            return communicateChannelName;
        }
        return CommunicateChannelEnum.codeDescription(communicateChannel);
    }
}
