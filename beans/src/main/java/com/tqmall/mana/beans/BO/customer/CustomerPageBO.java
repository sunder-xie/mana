package com.tqmall.mana.beans.BO.customer;

import com.tqmall.mana.beans.BO.shop.SimpleShopBO;
import com.tqmall.mana.component.enums.CustomerSourceEnum;
import com.tqmall.mana.component.enums.InsureIntentionEnum;
import com.tqmall.mana.component.enums.InsureStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouheng on 16/12/19.
 */
@Data
public class CustomerPageBO {
    /**
     * 客户ID
     */
    private Integer customerId;
    /**
     * 创建人(登记人)
     **/
    private String creator;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 登记时间
     */
    private Date regDate;
    /**
     * 车辆id
     */
    private Integer customerVehicleId;
    /**
     * 保单ID
     */
    private Integer insuranceFormId;
    /**
     * 门店类型1:门店,2:个人
     */
    private Integer agentType;
    /**
     * 代理人ID
     */
    private Integer agentId;
    /**
     * 门店信息
     */
    private SimpleShopBO simpleShopBO;

    /**
     * 客户手机号
     */
    private String customerMobile;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 车牌号
     */
    private String licencePlate;
    /**
     * 客户来源,门店,淘气
     */
    private Integer customerSource;
    /***/
    /**
     * 前端使用:客户来源 0:淘汽 1:门店
     */
    private String customerSourceName;

    /**
     * 保险公司名称
     */
    private String insureCompanyName;
    /**
     * 保单状态
     */
    private Integer insureStatus;

    /**
     * 外部用:车险状态 0:未投保 1:在保 2:脱保
     */
    private String insureStatusName;

    /**
     *     在淘汽投保状态 0:未投保 1:已投保 2:已退保
     */
    private Integer quitInsureStatus;

    /**
     * 保单结束日期
     */
    private Date insureEndDate;
    /**
     * 保单意向
     */
    private Integer insureIntention;

    /**
     * 投保意向 1:意向强 2:考虑中 3:无意向 4:已投保
     */
    private String insureIntentionName;

    /**
     * 和淘气合作模式 1:奖励金 2:买保险送服务 3:买服务送保险
     **/
    private Integer cooperationMode;
    /**
     * 保单合作模式描述
     */
    private String cooperationModeDescription;
    /**
     * 客户优惠记录默认前5次
     */
    private List<AddPreferentialLogBO> preferentialLogBOList;

    public String getCustomerSourceName() {
        if (customerSourceName != null) {
            return customerSourceName;
        }
        return CustomerSourceEnum.codeDescription(this.customerSource);
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

//    public String getCooperationModeDescription() {
//        if (cooperationModeDescription != null) {
//            return cooperationModeDescription;
//        }
//        return CooperationModeEnum.codeDescription(cooperationMode);
//    }
}
