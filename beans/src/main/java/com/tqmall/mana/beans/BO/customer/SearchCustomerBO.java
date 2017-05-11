package com.tqmall.mana.beans.BO.customer;

import com.tqmall.mana.beans.VO.CustomerPreferentialVO;
import com.tqmall.mana.beans.VO.InsuranceInfoVO;
import com.tqmall.mana.beans.entity.insurance.ManaInsuranceFormSyncLogDO;
import com.tqmall.mana.component.enums.CertificateTypeEnum;
import com.tqmall.mana.component.enums.CustomerSourceEnum;
import lombok.Data;

import java.util.List;

/**
 * Created by zhouheng on 16/12/16.
 */
@Data
public class SearchCustomerBO {
    /**
     * 客户id
     */
    private Integer id;

    private String customerMobile;

    private String customerName;

    private Integer customerSource;

    //前端使用:客户来源 0:淘汽 1:门店
    private String customerSourceName;

    private String certificateType;

    //前端,证件类型名称
    private String certificateTypeName;

    private String certificateNo;

    private String remarkInfo;

    private SearchCustomerAddressBO addressBO;

    private SearchCustomerVehicleBO vehicleBO;

    private Integer hasSync; //是否跟insurance同步过数据 0:否 1:是

    /** 业务信息-投保信息 */
    private List<InsuranceInfoVO> insuranceInfoVOList;
    /** 业务信息-优惠信息 */
    private List<CustomerPreferentialVO> preferentialVOList;

    public String getCustomerSourceName() {
        if (customerSourceName != null) {
            return customerSourceName;
        }
        return CustomerSourceEnum.codeDescription(this.customerSource);
    }

    public String getCertificateTypeName() {
        if (certificateTypeName != null) {
            return certificateTypeName;
        }
        return CertificateTypeEnum.codeDescription(certificateType);
    }
}
