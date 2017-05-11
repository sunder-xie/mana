package com.tqmall.mana.biz.manager.sms;

import com.tqmall.mana.beans.entity.sms.ManaSmsTemplateDO;
import com.tqmall.mana.component.annotation.cache.ManaCache;
import com.tqmall.mana.component.redis.RedisKeyBean;
import com.tqmall.mana.dao.mapper.sms.ManaSmsTemplateDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangzhangting on 16/12/9.
 */
@Service
@Slf4j
public class SmsBizImpl implements SmsBiz {
    @Autowired
    private ManaSmsTemplateDOMapper smsTemplateDOMapper;


    @ManaCache(cacheName = RedisKeyBean.SMS_TEMPLATE_KEY)
    @Override
    public List<ManaSmsTemplateDO> getAllTemplate() {
        List<ManaSmsTemplateDO> list = smsTemplateDOMapper.selectAll();
        if(list.isEmpty()){
            log.info("没有配置短信模板");
        }
        return list;
    }

}
