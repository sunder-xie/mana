package com.tqmall.mana.beans.BO.insurance;

import lombok.Data;

/**
 * Created by zhouheng on 17/2/20.
 */
@Data
public class InsuranceMaterialTemplateBO {

    /**主键ID**/
    private Integer id;

    /**创建时间**/
    private java.util.Date gmtCreate;

    /**商品sn**/
    private String goodsSn;

    /**服务包项对应的物料种类 0:其他 1:机滤 2:防冻液**/
    private Integer materialType;

    /**物料市场价格**/
    private java.math.BigDecimal marketPrice;

    /**物料成本价**/
    private java.math.BigDecimal materialPrice;

    /**物料型号**/
    private String materialModel;

    /**是否需要乘以项目次数:true时单次服务所需的物料数量,
     * false时服务项固定用量与服务次数无关***/
    private Boolean needMultiplyItemNums;

    /**商品数量***/
    private java.math.BigDecimal goodsNum;

}
