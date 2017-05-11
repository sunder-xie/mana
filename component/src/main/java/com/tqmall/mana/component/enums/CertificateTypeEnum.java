package com.tqmall.mana.component.enums;

/**
 * 证件类型（跟insurance保持一致）
 * Created by huangzhangting on 16/12/16.
 */
public enum CertificateTypeEnum {
    ID_CARD("120001", "居民身份证"),
    PASSPORT("120002", "护照"),
    MILITARY_CARD("120003", "军人证");

    private String code;
    private String desc;

    CertificateTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    //根据code获取code解释
    public static String codeDescription(String code){
        if(code==null){
            return null;
        }
        for(CertificateTypeEnum typeEnum : values()){
            if(typeEnum.getCode().equals(code)){
                return typeEnum.getDesc();
            }
        }
        return null;
    }

}
