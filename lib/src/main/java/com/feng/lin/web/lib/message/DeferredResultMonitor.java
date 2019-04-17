package com.feng.lin.web.lib.message;

public class DeferredResultMonitor {

	private volatile boolean timeout;

	public boolean isTimeout() {
		return timeout;
	}

	public void setTimeout(boolean timeout) {
		this.timeout = timeout;
	}


}
