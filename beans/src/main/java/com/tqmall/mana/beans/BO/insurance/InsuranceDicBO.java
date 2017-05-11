package com.tqmall.mana.beans.BO.insurance;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxg on 17/1/17.
 * 20:13
 * no bug,以后改代码的哥们，祝你好运~！！
 * 保险字典数据
 */
@Data
public class InsuranceDicBO {

    // 字典的唯一标识
    private Integer dicId;

    // 字典的内容，例如名称
    private String dicValue;

    public  InsuranceDicBO(){}
    public InsuranceDicBO(Integer dicId,String dicValue){
        this.dicId = dicId;
        this.dicValue = dicValue;
    }
}
