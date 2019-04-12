package com.feng.lin.test.demo.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;

public class Test extends AbstractModel implements Serializable {

	@Override
	public String toString() {
		return "Test [name=" + name + ", toString()=" + super.toString() + "]";
	}

	// 姓名
	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}