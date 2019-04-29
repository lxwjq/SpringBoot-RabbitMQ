package com.hxy.rabbitmq.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: LX 17839193044@162.com
 * @Description: topic模式消费者
 * @Date: 2019/4/29 16:22
 * @Version: V1.0
 */
@Component
public class TopicReceiver1 {

    @RabbitHandler
    @RabbitListener(queues = "topic.message")
    public void process(String message) {
        System.out.println("Topic Receiver1  : " + message);
    }
}
