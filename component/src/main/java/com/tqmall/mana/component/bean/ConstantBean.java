package com.tqmall.mana.component.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 16/12/7.
 */
public class ConstantBean {
    /* 重要信息切勿改动，后果自负~~~ */
    public static final String SEND_COUPON_KEY = "SEND_COUPON_KEY";
    public static final String ENCRYPT_KEY = "-120,-101,99,65,-5,-68,124,18,-108,66,-37,-23,67,-62,-31,50";
    /* ===========zhh============== */
    public static final String IS_DELETED_KEY_Y = "Y";
    public static final String IS_DELETED_KEY_N = "N";

    public static final String ACTION_ADD = "add";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_QUERY = "query";
    public static final String ACTION_DELETE = "delete";
    /*******************************/

    public static final String CURRENT_USER_KEY = "CURRENT_USER_KEY";

    /* openid登录url */
    public static final String OPENID_CONNECT_LOGIN = "/openid_connect_login";

    /* 系统名称，暂时先借用 这个值 */
    public static final String APPLICATION_NAME = "TQMALLPORTAL";

    /* 保费计算公式：保费变量 */
    public static final String INSURED_FEE_FORMULA_VARIABLE = "insuredFeeVariable";

    //安心保险公司id
    public static final Integer AN_XIN_INSURANCE_ID = 1;

    //云修门店标签
    public static final List<String> YUN_XIU_TAG_KEYS = new ArrayList<String>(){{
        add("FRANCHISEE_TYPE_A");
        add("FRANCHISEE_TYPE_B");
        add("FRANCHISEE_TYPE_C");
        add("SYC_FRANCHISEE_TYPE_A");
        add("SYC_FRANCHISEE_TYPE_B");
        add("SYC_FRANCHISEE_TYPE_C");
    }};

}
