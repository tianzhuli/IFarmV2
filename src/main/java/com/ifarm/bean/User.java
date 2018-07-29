package com.ifarm.bean;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	@Id
	private String userId;
	private String userPhone;
	private String userOtherAssociateId;
	private String userName;
	private String userPwd;
	private String userSex;
	private String userEmail;
	private String userRealName;
	private Date userRegisterTime;
	private Date userLastLoginTime;
	private String userBackImageUrl;
	private String userImageUrl;
	private String userSignature;
	private String userRole;

	public User() {

	}

	public User(String userId, String password) {
		this.userId = userId;
		this.userPwd = password;
	}

	public User(String userId, String userName, String userSex, Date userRegisterTime, Date userLastLoginTime, String userBackImageUrl,
			String userImageUrl, String userSignature) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userSex = userSex;
		this.userRegisterTime = userRegisterTime;
		this.userLastLoginTime = userLastLoginTime;
		this.userBackImageUrl = userBackImageUrl;
		this.userImageUrl = userImageUrl;
		this.userSignature = userSignature;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserOtherAssociateId() {
		return userOtherAssociateId;
	}

	public void setUserOtherAssociateId(String userOtherAssociateId) {
		this.userOtherAssociateId = userOtherAssociateId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public Date getUserRegisterTime() {
		return userRegisterTime;
	}

	public void setUserRegisterTime(Date userRegisterTime) {
		this.userRegisterTime = userRegisterTime;
	}

	public Date getUserLastLoginTime() {
		return userLastLoginTime;
	}

	public void setUserLastLoginTime(Date userLastLoginTime) {
		this.userLastLoginTime = userLastLoginTime;
	}

	public String getUserBackImageUrl() {
		return userBackImageUrl;
	}

	public void setUserBackImageUrl(String userBackImageUrl) {
		this.userBackImageUrl = userBackImageUrl;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getUserSignature() {
		return userSignature;
	}

	public void setUserSignature(String userSignature) {
		this.userSignature = userSignature;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		return super.toString();
	}

}
