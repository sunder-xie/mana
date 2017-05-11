package com.tqmall.mana.component.enums.insurance.dict;

import lombok.Getter;

/**
 * Created by zhouheng on 17/3/28.
 */
@Getter
public enum InsuranceOffLineAuditStatusEnum {


    DSH(0,"待审核"),
    SHTG(1,"审核通过"),
    SHBH(2,"审核驳回");

    private Integer code;

    private String name;

    InsuranceOffLineAuditStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    //根据code获取code解释
    public static String codeDescription(Integer code){
        if(code==null){
            return null;
        }
        for(InsuranceOffLineAuditStatusEnum enums : values()){
            if(enums.getCode().equals(code)){
                return enums.getName();
            }
        }
        return null;
    }


}
