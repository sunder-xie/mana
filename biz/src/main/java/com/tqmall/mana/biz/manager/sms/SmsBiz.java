package com.tqmall.mana.biz.manager.sms;

import com.tqmall.mana.beans.entity.sms.ManaSmsTemplateDO;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/9.
 */
public interface SmsBiz {

    List<ManaSmsTemplateDO> getAllTemplate();

}
