package com.tqmall.mana.beans.BO.insurance;

import lombok.Data;

/**
 * Created by zhouheng on 17/2/24.
 */
@Data
public class ServiceItemBO {

    /**主键ID**/
    private Integer id;

    /**服务项名称**/
    private String itemName;

    /**服务项次数**/
    private Integer itemTimes;

}
