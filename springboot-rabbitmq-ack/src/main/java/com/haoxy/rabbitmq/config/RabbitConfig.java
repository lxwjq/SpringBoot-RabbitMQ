package com.haoxy.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 配置文件类
 * @Date: 2019/4/29 16:34
 * @Version: V1.0
 */
@Slf4j
@Configuration
public class RabbitConfig {

    @Resource
    private RabbitConstants rabbitConstants;

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 配置连接工厂
     * @Date: 2019/4/29 16:34
     * @Version: V1.0
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitConstants.getHost());
        connectionFactory.setUsername(rabbitConstants.getUsername());
        connectionFactory.setVirtualHost(rabbitConstants.getVirtualHost());
        connectionFactory.setPassword(rabbitConstants.getPassword());
        // 如果要进行消息回调，则这里必须要设置为true
        connectionFactory.setPublisherConfirms(rabbitConstants.getPublisherConfirms());
        return connectionFactory;
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置
     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
     * @Date: 2019/4/29 16:18
     * @Version: V1.0
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        // 消息发送失败返回到队列中,配置上文件需要配置 publisher-returns = ture
        template.setMandatory(true);

        // 消息返回,配置上文件需要配置 publisher-returns = ture
        // 如果消息无法发送到指定的消息队列那么ReturnCallBack回调方法会被调用。
        template.setReturnCallback((message, replyCode, replyTest, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.debug("消息 : {} 发送失败，应答码 : {} 原因 : {} 交换机 : {} 路由键 : {}", correlationId, replyCode, replyTest, exchange, routingKey);
        });

        // 消息确认,配置上文件需要配置 publisher-confirms = ture
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送到exchange成功，ID : {}", correlationData.getId());
            } else {
                log.debug("消息发送到exchange失败，原因 : {}", cause);
            }
        });

        return template;
    }

    /**
     * 声明Direct交换机 支持持久化.
     *
     * @return the exchange
     */
    @Bean("directExchange")
    public Exchange directExchange() {
        return ExchangeBuilder.directExchange("DIRECT_EXCHANGE").durable(true).build();
    }

    /**
     * 声明一个队列 支持持久化.
     *
     * @return the queue
     */
    @Bean("directQueue")
    public Queue directQueue() {
        return QueueBuilder.durable("DIRECT_QUEUE").build();
    }

    /**
     * 通过绑定键 将指定队列绑定到一个指定的交换机 .
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding directBinding(@Qualifier("directQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("DIRECT_ROUTING_KEY").noargs();
    }

    /**
     * 声明 fanout 交换机.
     *
     * @return the exchange
     */
    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange() {
        return (FanoutExchange) ExchangeBuilder.fanoutExchange("FANOUT_EXCHANGE").durable(true).build();
    }

    /**
     * Fanout queue A.
     *
     * @return the queue
     */
    @Bean("fanoutQueueA")
    public Queue fanoutQueueA() {
        return QueueBuilder.durable("FANOUT_QUEUE_A").build();
    }

    /**
     * Fanout queue B .
     *
     * @return the queue
     */
    @Bean("fanoutQueueB")
    public Queue fanoutQueueB() {
        return QueueBuilder.durable("FANOUT_QUEUE_B").build();
    }

    /**
     * 绑定队列A 到Fanout 交换机.
     *
     * @param queue          the queue
     * @param fanoutExchange the fanout exchange
     * @return the binding
     */
    @Bean
    public Binding bindingA(@Qualifier("fanoutQueueA") Queue queue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    /**
     * 绑定队列B 到Fanout 交换机.
     *
     * @param queue          the queue
     * @param fanoutExchange the fanout exchange
     * @return the binding
     */
    @Bean
    public Binding bindingB(@Qualifier("fanoutQueueB") Queue queue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
}
