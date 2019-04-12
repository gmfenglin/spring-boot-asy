package com.feng.lin.web.lib.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.feng.lin.web.lib.config.property.ThreadPoolProperty;

@Configuration
@Import({ThreadPoolProperty.class})
public class ThreadPoolConfig {
	@Autowired
	private ThreadPoolProperty threadPoolProperty;

	@Bean
	public AsyncTaskExecutor asyncServiceExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 配置核心线程数
		executor.setCorePoolSize(threadPoolProperty.getCorePoolSize());
		// 配置最大线程数
		executor.setMaxPoolSize(threadPoolProperty.getMaxPoolSize());
		// 配置队列大小
		executor.setQueueCapacity(threadPoolProperty.getQueueCapacity());
		// 配置线程池中的线程的名称前缀
		executor.setThreadNamePrefix(threadPoolProperty.getThreadNamePrefix());

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 执行初始化
		executor.initialize();
		return executor;
	}
}
