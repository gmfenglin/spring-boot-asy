package com.feng.lin.web.lib.aop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

public class ControllerMethodArgumentNotValidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6849212991060938241L;
	private List<FieldError> fieldErrors=new ArrayList<>();
	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}
	

}
