package com.feng.lin.test.demo.dao.model;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class AbstractModel {
	@Min(1)
	private Integer id;
	@Min(0)
	@Max(1)
	private Integer status;
	@Min(0)
	@Max(1)
	private Integer isDeleted;
	private Date gmtCreate;
	private Date gmtModified;
	private Integer userModified;
	private Integer userCreate;
	private String createUserName;
	private String updateUserName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Integer getUserModified() {
		return userModified;
	}

	public void setUserModified(Integer userModified) {
		this.userModified = userModified;
	}

	public Integer getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(Integer userCreate) {
		this.userCreate = userCreate;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	@Override
	public String toString() {
		return "AbstractModel [id=" + id + ", status=" + status + ", isDeleted=" + isDeleted + ", gmtCreate="
				+ gmtCreate + ", gmtModified=" + gmtModified + ", userModified=" + userModified + ", userCreate="
				+ userCreate + ", createUserName=" + createUserName + ", updateUserName=" + updateUserName + "]";
	}

}