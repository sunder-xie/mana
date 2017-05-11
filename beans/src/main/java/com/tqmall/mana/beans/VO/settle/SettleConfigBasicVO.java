package com.tqmall.mana.beans.VO.settle;

import lombok.Data;

import java.util.List;

/**
 * Created by zhouheng on 17/1/13.
 */
@Data
public class SettleConfigBasicVO {

    /**
     * 组配置基础id
     */
    private Integer id;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 保险公司id
     */
    private Integer insuranceCompanyId;

    /**
     * 保险公司名称
     */
    private String insuranceCompanyName;

    /**
     * 和淘气合作模式 1:买保险送奖励金 2:买保险送服务 3:买服务送保险
     */
    private Integer cooperationMode;
    private String cooperationModeName;

    /**
     * 保险类别:1表示交强险,2表示商业险
     */
    private Integer insuranceType;
    private String insuranceTypeName;

    /**
     * 计费方式:1表示单笔,2表示月累计
     */
    private Integer calculateMode;
    private String calculateModeName;

    /**
     * 该组下比率阶梯
     */
    private List<SettleConfigItemVO> itemVOList;

}
