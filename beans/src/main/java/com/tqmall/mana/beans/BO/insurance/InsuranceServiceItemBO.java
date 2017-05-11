package com.tqmall.mana.beans.BO.insurance;

import lombok.Data;

import java.util.List;

/**
 * Created by zhouheng on 17/2/20.
 */
@Data
public class InsuranceServiceItemBO {

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

    /**服务项名称**/
    private String itemName;

    /**适用车型**/
    private String itemCarModel;

    /**内容与工序**/
    private String itemContentProcedure;

    /**作用和优点**/
    private String itemActionMerit;

    /**服务项单位**/
    private String itemUnit;

    /**服务项单价**/
    private java.math.BigDecimal itemPrice;

    /**服务项物料费**/
    private java.math.BigDecimal itemMaterialPrice;

    /**服务项工时费**/
    private java.math.BigDecimal itemWorkPrice;

    /**购买次数范围,下限(最低次数)**/
    private Integer buyMinNum;

    /**购买次数范围,上限(最高次数)**/
    private Integer buyMaxNum;

    /**服务项状态:0:下架 1:上架**/
    private Integer itemStatus;

    /**服务项目物料模版**/
    private List<InsuranceMaterialTemplateBO> materialTemplateParams;

    /**和服务项目相关联的视频**/
    List<InsuranceServiceItemVideoBO> serviceItemVideoParams;

}
