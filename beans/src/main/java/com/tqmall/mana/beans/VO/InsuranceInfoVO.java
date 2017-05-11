package com.tqmall.mana.beans.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 车主详情-业务信息
 * Created by huangzhangting on 16/12/29.
 */
@Data
public class InsuranceInfoVO {
    /** 创建时间 */
    private Date gmtCreate;
    /** 与淘气合作模式 */
    private String cooperationModeStr;

    /** 品牌车型 */
    private String configType;
    /** 支付金额 */
    private BigDecimal payAmount;
    /** 服务门店 */
    private String agentName;

    /** 是否虚拟保单 0:否 1:是 虚拟保单就是买服务包 */
    private Integer isVirtual;
    /** 服务包名字 */
    private String packageName;
    /** 服务包价格 */
    private BigDecimal marketPrice;
    /** 服务包物料总成本价 */
    private BigDecimal totalMaterialPrice;


    private Integer insuranceBasicId;

    /** 代理人id */
    private Integer agentId;


    private List<InsuranceFormInfoVO> formInfoVOList;


    /** 组装支付金额时使用 */
    private boolean canAddPayAmount = true;

}
