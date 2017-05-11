package com.tqmall.mana.beans.BO.customer;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.mana.beans.BO.outInsurance.InsuranceFormBO;
import com.tqmall.mana.component.enums.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouheng on 16/12/16.
 */
@Data
public class SearchCustomerVehicleBO {

    private Integer id;

    private Integer customerId;

    private String licencePlate;

    private Integer hasLicencePlate;
    //外部用:是否
    private String hasLicencePlateName;

    private Integer agentId; //卖保险代理人id

    private Integer insuranceFormId; //最新的保单id

    private String vehicleType;

    private Integer insureCompanyId;
    // 保险公司名称
    private String insureCompanyName;

    private Integer insureStatus;
    //外部用:车险状态 0:未投保 1:在保 2:脱保
    private String insureStatusName;

    private Date insureEndDate;

    private String insureProvince;

    private String insureProvinceCode;

    private String insureCity;

    private String insureCityCode;

    private String insureVehicleType;

    private String engineNo;

    private String vinNo;

    private Date vehicleRegDate;

    private Integer insureIntention;
    //是否跟insurance同步过数据 0:否 1:是
    private Integer hasSync;

    //投保意向 1:意向强 2:考虑中 3:无意向 4:已投保
    private String insureIntentionName;
    //在淘汽投保状态 0:未投保 1:已投保 2:已退保
    private Integer quitInsureStatus;
    //在淘汽投保状态名称 0:未投保 1:已投保 2:已退保
    private String quitInsureStatusName;

    private PagingResult<SearchCommunicateRecordBO> recordBOPagingResult;

    private List<InsuranceFormBO> insuranceFormBOList;

    public String getHasLicencePlateName() {
        if (hasLicencePlateName != null) {
            return hasLicencePlateName;
        }
        return YesNoEnum.codeDescription(hasLicencePlate);
    }

    public String getInsureStatusName() {
        if (insureStatusName != null) {
            return insureStatusName;
        }
        return InsureStatusEnum.codeDescription(insureStatus);
    }

    public String getInsureIntentionName() {
        if (insureIntentionName != null) {
            return insureIntentionName;
        }
        return InsureIntentionEnum.codeDescription(insureIntention);
    }

    public String getQuitInsureStatusName() {
        if (quitInsureStatusName != null) {
            return quitInsureStatusName;
        }
        return QuitInsureStatusEnum.codeDescription(quitInsureStatus);
    }
}
