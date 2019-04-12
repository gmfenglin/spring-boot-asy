package com.feng.lin.web.lib.controller;
public enum ResponseMessage {
	OK(200, "ok"), FAIL(100, "fail"), EXISTS(101, "exists"), SYSTEMERROR(500, "system error"),NODATA(102,"no data"),NOAUTH(401," Authentication failed"),FORBIDDEN(403,"Forbidden");
	private String message;
	private int code;

	ResponseMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
}