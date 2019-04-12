package com.feng.lin.test.demo.web.aop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import com.feng.lin.web.lib.aop.ControllerMethodArgumentNotValidException;
import com.feng.lin.web.lib.controller.Result;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ControllerMethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result<List<FieldError>> methodArgumentNotValidExceptionHandler(
			ControllerMethodArgumentNotValidException ex) {

		return new Result<List<FieldError>>().setSuccess(false).setCode(HttpStatus.BAD_REQUEST.value())
				.setData(ex.getFieldErrors()).setMessage(ex.getClass().getSimpleName());
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<String> handleUserNotExistException(RuntimeException ex) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", ex.getMessage());
		result.put("exception", ex.getClass().getSimpleName());
		return new Result<String>().setSuccess(false).setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.setData(ex.getMessage()).setMessage(ex.getClass().getSimpleName());
	}

	@ExceptionHandler(AsyncRequestTimeoutException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public  Result<String> handleUserNotExistException(AsyncRequestTimeoutException ex) {
		Map<String, Object> result = new HashMap<>();
		result.put("message", ex.getMessage());
		result.put("exception", ex.getClass().getSimpleName());
		return new Result<String>().setSuccess(false).setCode(HttpStatus.SERVICE_UNAVAILABLE.value())
				.setData(ex.getMessage()).setMessage(ex.getClass().getSimpleName());
	}
}
