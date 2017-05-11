package com.tqmall.mana.beans.param;

import lombok.Data;

/**
 * Created by zhouheng on 16/12/22.
 */
@Data
public class UcShopRequestParam {
    /**省份id*/
    private Integer provinceId;
    /**城市id*/
    private Integer cityId;
    /**区域id*/
    private Integer districtId;
    /**门店名称*/
    private String companyName;
    /**页码 从 1 开始 */
    private Integer pageNumber;
    /**每页个数*/
    private Integer pageSize;

}
