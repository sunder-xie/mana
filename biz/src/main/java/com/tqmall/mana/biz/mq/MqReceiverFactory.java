package com.tqmall.mana.biz.mq;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MqReceiverFactory {

    @Setter
    private String host;
    @Setter
    private Integer port;
    @Setter
    private String userName;
    @Setter
    private String password;
    @Setter
    private String virtualHost;
    @Setter
    private String mqSwitch;
    @Setter
    private List<MqReceiver> processList;

    private List<SimpleMessageListenerContainer> msgListeners = new ArrayList<>();


    public void init() {
        if (!"on".equals(mqSwitch)) {
            log.info("MqReceiverFactory 不启动, mqSwitch={}", mqSwitch);
            return;
        }
        log.info("MqReceiverFactory 开始启动");
        com.rabbitmq.client.ConnectionFactory rcf = new com.rabbitmq.client.ConnectionFactory();
        log.info("host={}, port={}", host, port);
        rcf.setHost(host);
        rcf.setPort(port);
        rcf.setUsername(userName);
        rcf.setPassword(password);
        rcf.setVirtualHost(virtualHost);
        ConnectionFactory cf = new CachingConnectionFactory(rcf);
        for (MqReceiver e : processList) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cf);
            MessageListenerAdapter pushOrderAdapter = new MessageListenerAdapter(e);
            container.setMessageListener(pushOrderAdapter);
            container.setQueueNames(e.getQueueName());
            container.setConcurrentConsumers(10);
            container.setMaxConcurrentConsumers(20);

            msgListeners.add(container);

            container.start();
        }
    }

    public void destroy() {
        for(SimpleMessageListenerContainer container : msgListeners) {
            container.destroy();
            log.info("MqReceiverFactory 关闭SimpleMessageListenerContainer成功");
        }
    }

}