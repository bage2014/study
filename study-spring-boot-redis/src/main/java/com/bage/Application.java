package com.bage;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import javax.annotation.Resource;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	// inject the template as ListOperations
	@Resource(name="redisTemplate")
	private ValueOperations<String, String> valueOperations;

	@Autowired
	StringRedisTemplate template;
	@Autowired
	CountDownLatch latch;


	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("192.168.146.133", 6379);
		config.setPassword(RedisPassword.of("bage"));
		return new JedisConnectionFactory(config);
	}
	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	Receiver receiver(CountDownLatch latch) {
		return new Receiver(latch);
	}

	@Bean
	CountDownLatch latch() {
		return new CountDownLatch(1);
	}

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		valueOperations.set("foo","bar bar");

		LOGGER.info("Sending message...");
		template.convertAndSend("chat", "Hello from Redis!");

		latch.await();
		System.out.println(valueOperations.get("foo"));
	}
}