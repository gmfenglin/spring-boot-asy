package com.feng.lin.web.lib.utils;

import java.util.function.Predicate;

public class ExecResult<I, H> {
	private I result;
	private H condition;

	private Predicate<H> execFlag;

	public I getResult() {
		return result;
	}

	public  ExecResult(I result, H condition, Predicate<H> execFlag) {
		super();
		this.result = result;
		this.condition = condition;
		this.execFlag = execFlag;
	}

	public boolean getExecFlag() {
		return execFlag.test(this.condition);
	}

}
