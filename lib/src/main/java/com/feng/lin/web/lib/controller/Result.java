package com.feng.lin.web.lib.controller;

public class Result <T>{
	private int code;
	private T data;
	private String message;
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
