package com.hxy.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: LX 17839193044@162.com
 * @Description: topic模式配置类
 * @Date: 2019/4/29 16:37
 * @Version: V1.0
 */
@Configuration
public class TopicRabbitConfig {

    final static String message = "topic.message";

    final static String messages = "topic.messages";

    @Bean
    public Queue queueMessage() {
        return new Queue(TopicRabbitConfig.message);
    }

    @Bean
    public Queue queueMessages() {
        return new Queue(TopicRabbitConfig.messages);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 交换机与队列进行绑定
     * @Date: 2019/4/29 16:38
     * @Version: V1.0
     */
    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 交换机与队列进行绑定
     * @Date: 2019/4/29 16:38
     * @Version: V1.0
     */
    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");//topic.messages   #: 表示一个或者多个
    }
}
