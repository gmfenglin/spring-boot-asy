package com.example.demo.web.config.aop;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import com.feng.lin.web.lib.aop.ControllerMethodArgumentNotValidException;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ControllerMethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> methodArgumentNotValidExceptionHandler(ControllerMethodArgumentNotValidException ex) {

		Map<String, Object> result = new HashMap<>();
		result.put("message", ex.getFieldErrors());
		result.put("exception", ex.getClass().getSimpleName());
		return result;
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> handleUserNotExistException(RuntimeException ex) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", ex.getMessage());
		result.put("exception", ex.getClass().getSimpleName());
		return result;
	}

	@ExceptionHandler(AsyncRequestTimeoutException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public Map<String, Object> handleUserNotExistException(AsyncRequestTimeoutException ex) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", ex.getMessage());
		result.put("exception", ex.getClass().getSimpleName());
		return result;
	}
}
