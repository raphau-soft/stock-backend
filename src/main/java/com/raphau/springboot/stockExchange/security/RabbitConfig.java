package com.raphau.springboot.stockExchange.security;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    Queue queueRequestBuyOffer() {
        return new Queue("buy-offer-request");
    }

    @Bean
    Queue queueRequestSellOffer() {
        return new Queue("sell-offer-request");
    }

    @Bean
    Queue queueRequestCompany() {
        return new Queue("company-request");
    }

    @Bean
    Queue queueTestDetails() {
        return new Queue("test-details-response");
    }

    @Bean
    Queue queueCpuData() {
        return new Queue("cpu-data-response");
    }

    @Bean
    Queue queueUserData() {
        return new Queue("user-data-request");
    }

    @Bean
    Queue queueStockData() {
        return new Queue("stock-data-request");
    }

    @Bean
    Queue queueUserDataResponse() {
        return new Queue("user-data-response");
    }

    @Bean
    Queue queueStockDataResponse() {
        return new Queue("stock-data-response");
    }

    @Bean
    Queue queueRegisterRequest() {
        return new Queue("register-request");
    }

    @Bean
    Queue queueRegisterResponse() {
        return new Queue("register-response");
    }

    @Bean
    Queue queueTradeRequest() {
        return new Queue("trade-request");
    }

    @Bean
    Queue queueTradeResponse() {
        return new Queue("trade-response");
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory co) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(co);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
        cachingConnectionFactory.setChannelCacheSize(1000000);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
}
