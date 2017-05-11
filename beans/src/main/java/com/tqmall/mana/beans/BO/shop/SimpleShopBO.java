package com.tqmall.mana.beans.BO.shop;

import lombok.Data;

/**
 * Created by huangzhangting on 16/12/23.
 */
@Data
public class SimpleShopBO {
    private Integer id;
    //门店名称
    private String companyName;

    //账号信息(随便取了一个账号的信息)
    private Integer defaultAccountId;
    private String defaultAccountRealName;
    private String defaultAccountMobile;

}
