package com.tqmall.mana.beans.param;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by zhouheng on 16/12/28.
 */
@Data
public class CommonVOPageParam {

    /**客户id*/
    private Integer customerId;
    /**前端传入参数:需要后端判断具体类型*/
    private String queryStr;
    /**客户车辆ID*/
    private Integer customerVehicleId;
    /**客户手机号*/
    private String customerMobile;
    /**客户名称*/
    private String customerName;
    /**车牌号*/
    private String licencePlate;
    /**客户来源,编码*/
    private Integer customerSource;
    /**合作模式,和淘气合作模式 1:奖励金 2:买保险送服务 3:买服务送保险*/
    private Integer cooperationMode;
    /**保险公司id*/
    private Integer insureCompanyId;
    /**保险意向*/
    private Integer insureIntention;
    /**保险状态*/
    private Integer insureStatus;
    /**保险开始时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date insureStartDate;
    /**保险结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date insureEndDate;
    /**推荐门店id*/
//    private Integer recommendShopId;
    /**推荐门店名称*/
    private String agentName;
    /**每页个数*/
    private Integer pageSize;
    /**页码*/
    private Integer pageIndex;
    /**位置*/
    private Integer offerSet;

}
