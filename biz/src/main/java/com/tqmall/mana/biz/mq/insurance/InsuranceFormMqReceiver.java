package com.tqmall.mana.biz.mq.insurance;

import com.tqmall.mana.biz.manager.customer.CustomerSyncBiz;
import com.tqmall.mana.biz.mq.MqReceiver;
import com.tqmall.mana.component.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class InsuranceFormMqReceiver extends MqReceiver {
    @Autowired
    private CustomerSyncBiz customerSyncBiz;


    @Override
    public void handleMessage(Object obj) {
        if (obj == null || !(obj instanceof String)) {
            log.error("not process", obj);
            return;
        }
        String str = (String) obj;
        log.info("start process insurance form message: " + str);

        try {
            InsuranceBasicMsg basicMsg = JsonUtil.strToObject(str, InsuranceBasicMsg.class);

            customerSyncBiz.syncCustomerInfo(basicMsg);

        } catch (Exception e) {
            log.error("process error, message: " + str, e);
        }
    }
}
