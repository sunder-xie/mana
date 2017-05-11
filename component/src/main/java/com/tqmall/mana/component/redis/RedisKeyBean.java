package com.tqmall.mana.component.redis;

/**
 * redis key 的管理
 */

public interface RedisKeyBean {
    /*===========变量自动为 final static*/
    //redis变量key的系统前缀
    String SYSTEM_PREFIX = "MaNa_";

    String NULL_OBJECT = "None";

    /*=============失效时间=======================================================================*/
    /**
     * 缓存时效 1分钟
     */
    int RREDIS_EXP_MINUTE = 60;

    /**
     * 缓存时效 10分钟
     */
    int RREDIS_EXP_MINUTES = 600;

    /**
     * 缓存时效 60分钟
     */
    int RREDIS_EXP_HOURS = 3600;

    /**
     * 缓存时效 1天
     */
    int RREDIS_EXP_DAY = 3600 * 24;

    /**
     * 缓存时效 1周
     */
    int RREDIS_EXP_WEEK = 3600 * 24 * 7;

    /**
     * 缓存时效 1月
     */
    int RREDIS_EXP_MONTH = 3600 * 24 * 30 * 7;

    /*=====================自定义的各种key=========================================================================*/

    //用户资源权限
    String USER_PERMISSIONS_KEY = SYSTEM_PREFIX + "user_permissions_%s";
    //用户菜单
    String USER_MENUS_KEY = SYSTEM_PREFIX + "user_menus_%s";


    //insurance那边配置的保险资格门店
    String INSURANCE_SHOP_IDS_KEY = SYSTEM_PREFIX + "insurance_shop_ids";

    // insurance 的保险公司
    String INSURANCE_COMPANY_KEY = SYSTEM_PREFIX + "insurance_company_cache";

    // 优惠券类型
    String COUPON_TYPE_KEY = SYSTEM_PREFIX + "coupon_type";
    // 短信模板
    String SMS_TEMPLATE_KEY = SYSTEM_PREFIX + "sms_template";
    //现金券开放地区
    String CASHCOUPON_OPENED_REGION_KEY = SYSTEM_PREFIX + "cash_coupon_opened_region";


    /*===== 字典变量的永久缓存======*/
    String FOREVER_APPLY_RANGE_KEY = SYSTEM_PREFIX+"forever_apply_range_dict";
    String FOREVER_COOPERATION_MODE_KEY = SYSTEM_PREFIX+"forever_cooperation_mode_dict";
    String FOREVER_SETTLE_CONDITION_KEY = SYSTEM_PREFIX+"forever_settle_condition_dict";
    String FOREVER_REBATE_STANDARD_KEY = SYSTEM_PREFIX+"forever_rebate_standard_dict";
    String FOREVER_FUND_TYPE_KEY = SYSTEM_PREFIX+"forever_fund_type_dict";
    String FOREVER_REBATE_TYPE_KEY = SYSTEM_PREFIX+"forever_rebate_type_dict";
    String FOREVER_CALCULATE_MODE_KEY = SYSTEM_PREFIX+"forever_calculate_mode_dict";
    String FOREVER_SCENARIO_TYPE_KEY = SYSTEM_PREFIX+"forever_scenario_type_dict";
    String FOREVER_SETTLE_RULE_KEY = SYSTEM_PREFIX+"forever_settle_rule_dict";
    String FOREVER_SETTLE_PROJECT_KEY = SYSTEM_PREFIX+"forever_settle_project_dict";
    String FOREVER_SETTLE_STATUS_KEY = SYSTEM_PREFIX+"forever_settle_status_dict";
    String FOREVER_INSURANCE_TYPE_KEY = SYSTEM_PREFIX+"forever_insurance_type_dict";
    String FOREVER_CONFIRM_MONEY_STATUS_KEY = SYSTEM_PREFIX+"forever_confirm_money_status_dict";
    String FOREVER_REWARD_STATUS_KEY = SYSTEM_PREFIX+"forever_reward_status_dict";
    String FOREVER_WITHDRAW_CASH_STATUS_KEY = SYSTEM_PREFIX+"forever_withdraw_cash_status_key";
    String FOREVER_BALANCE_STATUS_KEY = SYSTEM_PREFIX+"forever_balance_status_key";
    String FOREVER_ACCOUNT_INFO_KEY = SYSTEM_PREFIX+"forever_account_info_";

    /*========锁机制===========*/
    String LOCK_SETTLE_SHOP_BASE_BY_SHOP_ID = SYSTEM_PREFIX+"lock_settle_shop_base";
    String LOCK_UPDATE_CAR_OWNER_CONFIRM_MONEY_PAID=SYSTEM_PREFIX+"car_owner_confirm_money_paid_";
    String LOCK_CAR_OWNER_REVIEW=SYSTEM_PREFIX+"car_owner_review";
    String LOCK_CALCULATE_INSURANCE_ROYALTY_FEE=SYSTEM_PREFIX+"calculate_Insured_Royalty_Fee_";

    String LOCK_INSERT_SERVICE_CHECK_DETAIL = SYSTEM_PREFIX+"lock_insert_service_check_detail_";
    String LOCK_INSERT_SETTLE_FORM = SYSTEM_PREFIX+"lock_insert_settle_form_";
    String LOCK_INSERT_SHOP_CHECK_DETAIL = SYSTEM_PREFIX+"lock_insert_shop_check_detail_";
    String LOCK_INSERT_CAR_OWNER_CHECK_DETAIL = SYSTEM_PREFIX+"lock_insert_car_owner_check_detail_";
    String LOCK_CALCULATE_SETTLE_DATA = SYSTEM_PREFIX+"lock_calculate_settle_data";
    String LOCK_CALCULATE_MATERIAL_ALLOWANCE = SYSTEM_PREFIX+"lock_calculate_material_allowance";
    String LOCK_SYNC_SETTLE_DATA_BY_VEHICLE_SN = SYSTEM_PREFIX+"lock_sync_settle_data_by_vehicle_sn_";


    String LOCK_CALCULATE_SETTLE_DATA_EXTEND = SYSTEM_PREFIX + "lock_calculate_settle_data_extend";

}
