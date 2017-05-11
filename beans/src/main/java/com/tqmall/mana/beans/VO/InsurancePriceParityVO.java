package com.tqmall.mana.beans.VO;

import com.tqmall.mana.beans.BO.InsuranceBasicBO;
import lombok.Data;

/**
 * 保险比价页面数据展示对象
 *
 * Created by huangzhangting on 16/12/3.
 */
@Data
public class InsurancePriceParityVO {
    /** 保单ID, 关联insurance_basic.id */
    private Integer insuranceBasicId;

    /** 创建时间 */
    private String gmtCreateStr;

    /** 商业险单号 */
    private String vciNo;

    /** 交强险单号 */
    private String tciNo;

    /** 保险基本信息 */
    InsuranceBasicBO basicBO;

}
