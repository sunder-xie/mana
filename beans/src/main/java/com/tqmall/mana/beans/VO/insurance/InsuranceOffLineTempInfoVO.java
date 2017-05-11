package com.tqmall.mana.beans.VO.insurance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by zhouheng on 17/3/28.
 */
@Data
public class InsuranceOffLineTempInfoVO {

    /**主键ID**/
    private Integer id;

    /**卖保险代理人id**/
    private Integer agentId;

    /**卖保险代理人名称**/
    private String agentName;

    /**保险公司id, 关联insurance_company.id**/
    private Integer insuranceCompanyId;

    /**车牌号码**/
    private String vehicleSn;

    /**商业险保单号**/
    private String commercialFormNo;

    /**商业险保费**/
    private java.math.BigDecimal commercialFee;

    /**商业险起保日期**/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtCommercialStart;

    /**交强险保单号**/
    private String forcibleFormNo;

    /**交强险保费**/
    private java.math.BigDecimal forcibleFee;

    /**交强险起保日期**/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtForcibleStart;

    /**支付(签单)日期**/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtPay;

    /**投保所在省**/
    private String insuredProvinceCode;

    /**投保所在城市**/
    private String insuredCityCode;

    /**投保所在省**/
    private String insuredProvince;

    /**投保所在城市**/
    private String insuredCity;

    /**车船税**/
    private java.math.BigDecimal vesselTaxFee;

    /**保单审核状态, 0:未审核 1:审核通过 2:审核驳回**/
    private Integer auditStatus;

    /**审核日期**/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date gmtAudit;

    /**驳回原因**/
    private String rejectReason;

    /**保单基础表id,关联insurance_basc.id**/
    private Integer insuranceBascId;

    /**审核人ID**/
    private Integer auditor;

    /**审核人名称*/
    private String auditName;

}
