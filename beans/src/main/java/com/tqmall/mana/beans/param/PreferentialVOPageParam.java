package com.tqmall.mana.beans.param;

import lombok.Data;

/**
 * Created by zhouheng on 16/12/30.
 */
@Data
public class PreferentialVOPageParam {

    /**客户车辆ID*/
    private Integer customerVehicleId;

    /**每页个数*/
    private Integer pageSize;
    /**页码*/
    private Integer pageIndex;

}
