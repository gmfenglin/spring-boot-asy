package com.feng.lin.web.lib.controller;

import com.fasterxml.jackson.annotation.JsonView;

public class Result <T>{
	public interface ResultView{
		
	}
	@JsonView(ResultView.class)
	private int code;
	@JsonView(ResultView.class)
	private T data;
	@JsonView(ResultView.class)
	private String message;
	@JsonView(ResultView.class)
	private boolean success;
	
	public int getCode() {
		return code;
	}
	public Result <T> setCode(int code) {
		this.code = code;
		return this;
	}
	public T getData() {
		return data;
	}
	public Result <T> setData(T data) {
		this.data = data;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public Result <T> setMessage(String message) {
		this.message = message;
		return this;
	}
	public boolean isSuccess() {
		return success;
	}
	public Result <T> setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	
}
