package com.feng.lin.web.lib.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.feng.lin.web.lib.aop.AsyControllerAspect;

@Configuration
@Import({ TimeInterceptor.class, ThreadPoolConfig.class, AsyControllerAspect.class })
public class WebConfig extends WebMvcConfigurationSupport {
	@Autowired
	private TimeInterceptor timeInterceptor;
	@Autowired
	private AsyncTaskExecutor asyncTaskExecutor;

	@Override
	protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.registerDeferredResultInterceptors(timeInterceptor);
		configurer.setTaskExecutor(asyncTaskExecutor);
		super.configureAsyncSupport(configurer);
	}

}
