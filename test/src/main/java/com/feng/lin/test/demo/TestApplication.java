package com.feng.lin.test.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.feng.lin.test.demo.message.Receiver;
import com.feng.lin.web.lib.controller.annotation.EnableFenglin;

@SpringBootApplication
@MapperScan(value = "com.feng.lin.test.demo.dao.mapper")
@EnableFenglin
@EnableTransactionManagement
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@Autowired
	private Receiver receiver;

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("mytopic"));

		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter() {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
}
