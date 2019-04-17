package com.feng.lin.web.lib.config;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
@Component
public class TimeInterceptor implements DeferredResultProcessingInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(TimeInterceptor.class);

	@Override
	public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult)
			throws Exception {
	}

	@Override
	public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
		long startTime = new Date().getTime();
		request.setAttribute("startTime", startTime, RequestAttributes.SCOPE_REQUEST);
		if (logger.isDebugEnabled()) {
			logger.debug("startTime:" + startTime);

		}
	}

	@Override
	public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult, Object concurrentResult)
			throws Exception {
	}

	@Override
	public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
		if (logger.isDebugEnabled()) {
			long startTime = new Date().getTime();
			logger.debug("handleTimeout:" + startTime);

		}
		
		return !deferredResult.hasResult();
	}

	@Override
	public <T> boolean handleError(NativeWebRequest request, DeferredResult<T> deferredResult, Throwable t)
			throws Exception {
		if (logger.isDebugEnabled()) {
			long startTime = new Date().getTime();
			logger.debug("handleError:" + startTime);

		}
		return DeferredResultProcessingInterceptor.super.handleError(request, deferredResult, t);
	}

	@Override
	public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
		Long startTime = (Long) request.getAttribute("startTime", RequestAttributes.SCOPE_REQUEST);
		if (logger.isDebugEnabled()) {

			logger.debug(" time interceptor 耗时:" + (new Date().getTime() - startTime));

		}
	}

}