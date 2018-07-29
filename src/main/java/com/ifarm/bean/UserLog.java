package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_log")
public class UserLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userlogId;
	private String userId;
	private String userIp;
	private String userOperateType;
	private String userOperateObject;
	private Timestamp logCreateTime;
	private String requestMessage;
	private String returnMessage;

	public void setUserlogId(Integer userlogId) {
		this.userlogId = userlogId;
	}

	public Integer getUserlogId() {
		return userlogId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserOperateType(String userOperateType) {
		this.userOperateType = userOperateType;
	}

	public String getUserOperateType() {
		return userOperateType;
	}

	public void setUserOperateObject(String userOperateObject) {
		this.userOperateObject = userOperateObject;
	}

	public String getUserOperateObject() {
		return userOperateObject;
	}

	public void setLogCreateTime(Timestamp logCreateTime) {
		this.logCreateTime = logCreateTime;
	}

	public Timestamp getLogCreateTime() {
		return logCreateTime;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public String getReturnMessage() {
		return returnMessage;
	}
}
