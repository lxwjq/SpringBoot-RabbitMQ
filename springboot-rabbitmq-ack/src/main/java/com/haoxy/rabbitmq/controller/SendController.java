package com.haoxy.rabbitmq.controller;

import com.haoxy.rabbitmq.model.ResponseEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 测试controller
 * @Date: 2019/4/29 16:35
 * @Version: V1.0
 */
@RestController
@RequestMapping("/rabbitmq")
public class SendController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试Direct模式.
     *
     * @param p the p
     * @return the response entity
     */
    @RequestMapping("/direct")
    public ResponseEntity direct(String p) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("DIRECT_EXCHANGE", "DIRECT_ROUTING_KEY", p, correlationData);
        return ResponseEntity.ok();
    }

    /**
     * 测试广播模式.
     *
     * @param p the p
     * @return the response entity
     */
    @RequestMapping("/fanout")
    public ResponseEntity send(String p) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("FANOUT_EXCHANGE", "", p, correlationData);
        return ResponseEntity.ok();
    }
}
