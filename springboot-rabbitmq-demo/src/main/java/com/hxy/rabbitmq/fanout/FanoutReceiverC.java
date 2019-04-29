package com.hxy.rabbitmq.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: LX 17839193044@162.com
 * @Description: Fanout模式消费者
 * @Date: 2019/4/29 16:38
 * @Version: V1.0
 */
@Component
public class FanoutReceiverC {
    @RabbitHandler
    @RabbitListener(queues = "fanout.C")
    public void process(String message) {
        System.out.println("fanout Receiver C: " + message);
    }
}
