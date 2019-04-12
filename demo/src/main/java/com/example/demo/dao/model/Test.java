package com.example.demo.dao.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

public class Test {
	@Min(12)
	@Max(15)
	@NotNull
	private Integer testId;
	@NotNull
	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	@Override
	public String toString() {
		return "Test [testId=" + testId + ", name=" + name + "]";
	}

	
}