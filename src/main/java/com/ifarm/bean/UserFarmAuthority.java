package com.ifarm.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_farm_authority")
public class UserFarmAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer authId;
	private String userId;
	private Integer farmId;
	private String authority;

	public UserFarmAuthority() {

	}

	public UserFarmAuthority(String userId, Integer farmId, String authority) {
		this.userId = userId;
		this.farmId = farmId;
		this.authority = authority;
	}

	public Integer getAuthId() {
		return authId;
	}

	public void setAuthId(Integer authId) {
		this.authId = authId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getFarmId() {
		return farmId;
	}

	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
