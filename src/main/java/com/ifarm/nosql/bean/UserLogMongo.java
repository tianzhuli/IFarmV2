package com.ifarm.nosql.bean;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userlog")
public class UserLogMongo {
	private Integer userlogId;
	private String userId;
	private String userIp;
	private String userOperateType;
	private String userOperateObject;
	private Date logCreateTime;
	private String requestMessage;
	private String returnMessage;

	public Integer getUserlogId() {
		return userlogId;
	}

	public void setUserlogId(Integer userlogId) {
		this.userlogId = userlogId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserOperateType() {
		return userOperateType;
	}

	public void setUserOperateType(String userOperateType) {
		this.userOperateType = userOperateType;
	}

	public String getUserOperateObject() {
		return userOperateObject;
	}

	public void setUserOperateObject(String userOperateObject) {
		this.userOperateObject = userOperateObject;
	}

	public Date getLogCreateTime() {
		return logCreateTime;
	}

	public void setLogCreateTime(Date logCreateTime) {
		this.logCreateTime = logCreateTime;
	}

	public void setLogCreateTime(Timestamp logCreateTime) {
		this.logCreateTime = logCreateTime;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
}
