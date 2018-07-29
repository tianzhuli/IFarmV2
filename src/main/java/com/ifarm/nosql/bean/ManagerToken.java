package com.ifarm.nosql.bean;

import java.io.Serializable;

public class ManagerToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -12359845762667L;
	private String managerId;
	private String token;
	private String tokenTime;

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenTime() {
		return tokenTime;
	}

	public void setTokenTime(String tokenTime) {
		this.tokenTime = tokenTime;
	}

}
