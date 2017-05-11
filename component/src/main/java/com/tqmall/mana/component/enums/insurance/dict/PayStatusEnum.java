package com.tqmall.mana.component.enums.insurance.dict;

/**
 * Created by jinju.zeng on 17/2/8.
 */
public enum PayStatusEnum {
    NO_PAY(0,"未支付"),
    PAY(1,"已支付");

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    PayStatusEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
