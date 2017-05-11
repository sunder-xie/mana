package com.tqmall.mana.biz.mq;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MqSenderInfo {

    private String senderName;

    private String exchange;

    private String routingKey;
}
