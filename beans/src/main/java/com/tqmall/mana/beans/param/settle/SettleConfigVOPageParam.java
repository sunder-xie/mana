package com.tqmall.mana.beans.param.settle;

import lombok.Data;

/**
 * Created by zhouheng on 17/1/14.
 */
@Data
public class SettleConfigVOPageParam {

    /**
     * 保险公司id
     */
    private Integer insuranceCompanyId;

    /**
     * 和淘气合作模式 1:买保险送奖励金 2:买保险送服务 3:买服务送保险
     */
    private Integer cooperationMode;

    /**
     * 保险类别:1表示交强险,2表示商业险
     */
    private Integer insuranceType;

    /**
     * 计费方式:1表示单笔,2表示月累计
     */
    private Integer calculateMode;

    /**
     * 分页偏移量
     */
    private Integer offerSet;

    /**
     * 分页页码
     */
    private Integer pageNumber;

    /**
     * 每页长度
     */
    private Integer pageSize;


}
