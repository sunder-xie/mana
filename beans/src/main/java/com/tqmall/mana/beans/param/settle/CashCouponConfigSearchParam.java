package com.tqmall.mana.beans.param.settle;

import lombok.Data;

/**
 * Created by zhouheng on 17/4/11.
 */
@Data
public class CashCouponConfigSearchParam {

    private String provinceName;//省份名字

    private String cityName;//城市名字

    private Integer pageSize;

    private Integer pageNumber;

}
