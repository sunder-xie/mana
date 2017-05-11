package com.tqmall.mana.biz.mq;

import lombok.Getter;
import lombok.Setter;

public abstract class MqReceiver {

    @Getter
    @Setter
    private String queueName;

    public abstract void handleMessage(Object obj);

}
