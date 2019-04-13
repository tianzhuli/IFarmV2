package com.ifarm.bean;

/**
 * manager是指各类管理人员
 */
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "manager")
@DynamicUpdate(true)
public class Manager {
	@Id
	private String managerId;
	private String managerName;
	private String managerPwd;
	private String roler;

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerPwd() {
		return managerPwd;
	}

	public void setManagerPwd(String managerPwd) {
		this.managerPwd = managerPwd;
	}
	
	public String getRoler() {
		return roler;
	}

	public void setRoler(String roler) {
		this.roler = roler;
	}
}
