package com.haoxy.rabbitmq.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: LX 17839193044@162.com
 * @Description: rabbitmq配置文件常量类
 * @Date: 11:40 2019/4/24
 * @Version: V1.0
 */
@Data
@ToString
@Component("simpleRabbitMqConfig")
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitConstants {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private Boolean publisherConfirms;

    private Boolean publisherReturns;

    private String virtualHost;

}
