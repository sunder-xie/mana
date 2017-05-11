package com.tqmall.mana.beans.BO.sys;

import lombok.Data;

/**
 * Created by huangzhangting on 16/12/26.
 */
@Data
public class SearchUserBO {
    private String staffNo; //工号
    private String mobile; //手机号
    private String name; //姓名

    private Integer pageIndex;
    private Integer pageSize;
}
