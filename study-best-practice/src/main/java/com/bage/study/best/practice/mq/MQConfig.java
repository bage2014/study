package com.bage.study.best.practice.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    static final String topicExchangeName = "study.best.practice.exchange";
    static final String queueName = "com.bage.study.best.practice.mq";
    public static final String queueKey = "best.practice.user";
    
    // 性能测试相关配置
    static final String performanceExchangeName = "performance.exchange";
    static final String performanceQueueName = "performance.queue";
    static final String performanceRoutingKey = "performance.routing.key";

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
    }
    
    @Bean
    public Queue performanceQueue() {
        return new Queue(performanceQueueName, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchangeName);
    }
    
    @Bean
    public TopicExchange performanceExchange() {
        return new TopicExchange(performanceExchangeName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueKey);
    }
    
    @Bean
    public Binding performanceBinding(Queue performanceQueue, TopicExchange performanceExchange) {
        return BindingBuilder.bind(performanceQueue).to(performanceExchange).with(performanceRoutingKey);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    
    @Bean
    public SimpleMessageListenerContainer performanceMessageListenerContainer(ConnectionFactory connectionFactory,
            MessageListenerAdapter performanceListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(performanceQueueName);
        container.setMessageListener(performanceListenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(UserMessageReceiver userMessageReceiver) {
        return new MessageListenerAdapter(userMessageReceiver, "receiveMessage");
    }
    
    @Bean
    public MessageListenerAdapter performanceListenerAdapter(UserMessageReceiver userMessageReceiver) {
        return new MessageListenerAdapter(userMessageReceiver, "receiveMessage");
    }

}
