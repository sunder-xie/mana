package com.tqmall.mana.biz.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huangzhangting on 16/12/20.
 */
@Slf4j
//@Service
public class ManaInsuranceFormSender {
    @Autowired
    private MqSendFactory mqSendFactory;

    public boolean sendInsuranceForm(Object object){
        log.info("send insurance form to mana");
        return mqSendFactory.doSend("manaInsuranceForm", object);
    }
}
