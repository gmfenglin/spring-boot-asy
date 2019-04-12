package com.example.demo.web.config;

public class DataSourceContextHolder {
	private static final ThreadLocal<String> local = new ThreadLocal<String>();

	public static void read() {
		local.set(DataSourceType.READ.getType());
	}

	public static void write() {
		local.set(DataSourceType.WRITE.getType());
	}

	public static String getJdbcType() {
		if (local.get() == null) {
			write();
		}
		return local.get();
	}
}
