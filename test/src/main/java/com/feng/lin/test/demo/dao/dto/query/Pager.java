package com.feng.lin.test.demo.dao.dto.query;


public class Pager {
	private int start;
	private int limit;
	private int currentPage;
	private String orderFiled;
	private String desc;
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getOrderFiled() {
		return orderFiled;
	}
	public void setOrderFiled(String orderFiled) {
		this.orderFiled = orderFiled;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
