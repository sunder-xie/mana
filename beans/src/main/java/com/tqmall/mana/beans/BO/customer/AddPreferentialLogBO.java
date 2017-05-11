package com.tqmall.mana.beans.BO.customer;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhouheng on 16/12/22.
 */
@Data
public class AddPreferentialLogBO {

    private Integer id;

    private String creator;

    private Integer customerVehicleId;

    private String customerMobile;

    private Date sendDate;

    private String preferentialContent;

    private Integer preferentialType;
    /**优惠类型名称,前端用到*/
    private String preferentialTypeName;

    private String preferentialInfo;


    private Integer preferentialNum;

    public String getPreferentialInfo() {
        if(preferentialInfo != null){
            return preferentialInfo;
        }

        return this.sendDate+" "+preferentialTypeName+" X "+preferentialNum;
    }
}
