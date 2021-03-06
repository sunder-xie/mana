package com.tqmall.mana.beans.BO.settle;

import com.tqmall.mana.component.enums.insurance.dict.InsuranceTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * Created by zhouheng on 17/1/13.
 */
@Data
public class SettleConfigBasicBO {

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

    /**
     * 前端使用:和淘气合作模式 1:买保险送奖励金 2:买保险送服务 3:买服务送保险
     */
    private String cooperationModeName;

    /**
     * 保险类别:1表示交强险,2表示商业险
     */
    private Integer insuranceType;

    /**
     * 前端使用:保险类别:1表示交强险,2表示商业险
     */
    private String insuranceTypeName;

    /**
     * 计费方式:1表示单笔,2表示月累计
     */
    private Integer calculateMode;

    /**
     * 前端使用:计费方式:1表示单笔,2表示月累计
     */
    private String calculateModeName;

    /**
     * 是否使用中:0否,1是
     */
    private Integer hasUsed;

    /**
     * 该组下比率阶梯
     */
    private List<SettleConfigItemBO> itemBOList;

    public String getInsuranceTypeName() {
        if(insuranceTypeName != null){
            return insuranceTypeName;
        }
        return InsuranceTypeEnum.codeDescription(insuranceType);
    }
}
