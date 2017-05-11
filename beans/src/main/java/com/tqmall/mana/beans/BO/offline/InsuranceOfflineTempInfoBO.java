package com.tqmall.mana.beans.BO.offline;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhouheng on 17/3/24.
 */
@Data
public class InsuranceOfflineTempInfoBO {

    /**
     * 主键ID
     **/
    private Integer id;

    /**
     * 创建时间
     **/
    private java.util.Date gmtCreate;

    /**
     * 卖保险代理人id
     **/
    private Integer agentId;

    /**
     * 卖保险代理人名称
     **/
    private String agentName;

    /**
     * 买保险代理人手机号
     */
    private String agentMobile;

    /**
     * 保险公司id, 关联insurance_company.id
     **/
    private Integer insuranceCompanyId;

    /**
     * 保险公司名称
     **/
    private String insuranceCompanyName;

    /**
     * 车牌号码
     **/
    private String vehicleSn;

    /**
     * 商业险保单号
     **/
    private String commercialFormNo;

    /**
     * 商业险保费
     **/
    private java.math.BigDecimal commercialFee;

    /**
     * 商业险起保日期
     **/
    private java.util.Date gmtCommercialStart;

    /**
     * 交强险保单号
     **/
    private String forcibleFormNo;

    /**
     * 交强险保费
     **/
    private java.math.BigDecimal forcibleFee;

    /**
     * 交强险起保日期
     **/
    private java.util.Date gmtForcibleStart;

    /**
     * 车船税
     **/
    private java.math.BigDecimal vesselTaxFee;

    /**
     * 保单审核状态, 0:未审核 1:审核通过 2:审核驳回
     **/
    private Integer auditStatus;

    /**
     * 保单审核状态名称, 0:未审核 1:审核通过 2:审核驳回
     **/
    private String auditStatusName;

    /**
     * 审核日期
     **/
    private java.util.Date gmtAudit;

    /**
     * 驳回原因
     **/
    private String rejectReason;

    /**
     * 保单基础表id,关联insurance_basc.id
     **/
    private Integer insuranceBascId;

    /**
     * 审核人ID
     **/
    private Integer auditor;

    /**审核人名称*/
    private String auditName;

    public void setCommercialFee(BigDecimal commercialFee) {
        if (commercialFee != null && commercialFee.compareTo(BigDecimal.ZERO) == 1) {
            this.commercialFee = commercialFee;
        }else{
            this.commercialFee = null;
        }
    }

    public void setForcibleFee(BigDecimal forcibleFee) {
        if (forcibleFee != null && forcibleFee.compareTo(BigDecimal.ZERO) == 1) {
            this.forcibleFee = forcibleFee;
        }else{
            this.forcibleFee = null;
        }
    }

    public void setVesselTaxFee(BigDecimal vesselTaxFee) {
        if (vesselTaxFee != null && vesselTaxFee.compareTo(BigDecimal.ZERO) == 1) {
            this.vesselTaxFee = vesselTaxFee;
        }else{
            this.vesselTaxFee = null;
        }
    }
}
