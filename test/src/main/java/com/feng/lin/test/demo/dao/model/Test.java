package com.feng.lin.test.demo.dao.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Test")
public class Test extends AbstractModel implements Serializable {
	public interface Save {

	}

	public interface ModifiedNull {

	}

	public interface ModifiedNotNull {

	}

	// 姓名
	@NotBlank(groups = {Save.class,ModifiedNotNull.class} )
	@Null(groups = ModifiedNull.class)
	@ApiModelProperty(value = "name")
	private String name;
	// 姓名
	@NotBlank(groups = {Save.class,ModifiedNotNull.class} )
	@ApiModelProperty(value = "tname")
	private String tname;

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

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