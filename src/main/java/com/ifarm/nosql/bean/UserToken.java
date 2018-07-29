package com.ifarm.nosql.bean;

import java.io.Serializable;

public class UserToken implements Serializable {

	private static final long serialVersionUID = -12359845762666L;
	private String userId;
	private String tokenId;
	private String tokenTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getTokenTime() {
		return tokenTime;
	}

	public void setTokenTime(String tokenTime) {
		this.tokenTime = tokenTime;
	}
}
