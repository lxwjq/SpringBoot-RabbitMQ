package com.hxy.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

    @Bean
    public Queue hxyQueue() {
        return new Queue("neo");
    }

    @Bean
    public Queue ObjectQueue() {
        return new Queue("object");
    }
}
