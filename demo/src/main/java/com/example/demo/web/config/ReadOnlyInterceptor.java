package com.example.demo.web.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReadOnlyInterceptor implements Ordered {

	@Around("@annotation(readOnly)")
	public Object setRead(ProceedingJoinPoint joinPoint, ReadOnly readOnly) throws Throwable {
		try {
			DataSourceContextHolder.read();
			return joinPoint.proceed();
		} finally {
			DataSourceContextHolder.write();
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}
}