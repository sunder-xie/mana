package com.tqmall.mana.biz.mq;

import com.tqmall.mana.component.util.JsonUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

@Slf4j
public class MqSendFactory {

    @Setter
    private RabbitTemplate rabbitTemplate;


    /**
     * key 为发送者name, 自己定义
     * 配置在xml文件中
     */
    @Setter
    private Map<String, MqSenderInfo> senderMap;

    @Setter
    private String mqSwitch;


    /**
     * @param senderName 消息的发送者名称, 即在senderMap中注册过的消息发送者
     * @param obj        发送的消息对象
     * @return true 发送成功, false 发送失败
     */
    public boolean doSend(String senderName, Object obj) {
        if(!"on".equals(mqSwitch)){
            log.info("不发送消息, mqSwitch={}", mqSwitch);
            return false;
        }

        MqSenderInfo sender = senderMap.get(senderName);
        if (sender == null) {
            log.error("发送消息, senderName: " + senderName + " 没有配置或参数传递有误");
            return false;
        }
        String body;
        if (obj instanceof String) {
            body = (String) obj;
        } else {
            body = JsonUtil.objectToStr(obj);
        }
        if (body == null) {
            log.error("发送消息, senderName: " + senderName + "发送的消息内容为空或存在异常, 无法转换为json字符串");
            return false;
        }
        try {
            rabbitTemplate.convertAndSend(sender.getExchange(), sender.getRoutingKey(), body);
            log.info(senderName + " send mq: " + body);
            return true;
        } catch (Exception e) {
            log.error("send notify failed! body: " + body, e);
            return false;
        }
    }


}
