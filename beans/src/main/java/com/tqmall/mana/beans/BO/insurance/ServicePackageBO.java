package com.tqmall.mana.beans.BO.insurance;

import lombok.Data;

import java.util.List;

/**
 * Created by zhouheng on 17/2/24.
 */
@Data
public class ServicePackageBO {

    /**主键ID**/
    private Integer id;

    /**创建时间**/
    private java.util.Date gmtCreate;

    /**创建人ID**/
    private Integer creator;

    /**更新时间**/
    private java.util.Date gmtModified;

    /**更新人ID**/
    private Integer modifier;

    /**是否删除,Y删除,N未删除**/
    private String isDeleted;

    /**服务包名字**/
    private String packageName;

    /**服务包市场价格**/
    private java.math.BigDecimal marketPrice;

    /**服务包适配价格左区间**/
    private java.math.BigDecimal suitableStartPrice;

    /**服务包适配价格右区间**/
    private java.math.BigDecimal suitableEndPrice;

    /**门店现金返利**/
    private java.math.BigDecimal rewardAmount;

    /**服务包对应的机油商品sn**/
    private String engineOilSn;

    /**服务包描述**/
    private String description;

    /**机油规格，如：4 代表4L**/
    private Integer engineOilCapacity;

    /**服务包状态:0:下架 1:上架**/
    private Integer servicePackageStatus;

    /**根据服务包价格档位进行返现**/
    private Integer packageLevel;

    /**服务包营销语**/
    private String promtNote;

    /**服务包对应的服务项参数封装**/
    List<ServiceItemBO> serviceItemParams;

}
