package com.feng.lin.test.demo.dao.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Test")
public class Test extends AbstractModel implements Serializable {

	// 姓名
	@NotBlank
	@ApiModelProperty(value = "name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Test [name=" + name + ", toString()=" + super.toString() + "]";
	}

}