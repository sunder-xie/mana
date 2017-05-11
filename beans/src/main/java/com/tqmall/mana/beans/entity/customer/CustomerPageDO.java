package com.tqmall.mana.beans.entity.customer;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhouheng on 16/12/19.
 */
@Data
public class CustomerPageDO {

    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 创建人
     **/
    private String creator;
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
     * 代理人ID(门店ID)
     */
    private Integer agentId;

    /**
     * 门店名称
     */
    private String agentName;

    /**
     * 手机号
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
     * 客户来源
     */
    private Integer customerSource;
    /**
     * 合作模式
     */
    private Integer cooperationMode;
    /**
     * 保险公司id
     */
    private Integer insureCompanyId;
    /**
     * 保险状态
     */
    private Integer insureStatus;
    /**
     * 保单结束日期
     */
    private Date insureEndDate;
    /**
     * 保险意向
     */
    private Integer insureIntention;

    /**
     *     在淘汽投保状态 0:未投保 1:已投保 2:已退保
     */
    private Integer quitInsureStatus;



}
