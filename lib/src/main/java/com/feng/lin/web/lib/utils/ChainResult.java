package com.feng.lin.web.lib.utils;

public class ChainResult<T> {
	boolean flag;
	T result;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public T getResult() {
		return result;
	}

	public ChainResult(boolean flag, T result) {
		super();
		this.flag = flag;
		this.result = result;
	}
}