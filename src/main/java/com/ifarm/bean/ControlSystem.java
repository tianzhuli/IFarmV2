package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "control_system")
public class ControlSystem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer farmId;
	private String systemCode;
	private String systemType;
	private String systemTypeCode;
	private Timestamp systemCreateTime;
	private String systemStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFarmId() {
		return farmId;
	}

	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getSystemTypeCode() {
		return systemTypeCode;
	}

	public void setSystemTypeCode(String systemTypeCode) {
		this.systemTypeCode = systemTypeCode;
	}

	public Timestamp getSystemCreateTime() {
		return systemCreateTime;
	}

	public void setSystemCreateTime(Timestamp systemCreateTime) {
		this.systemCreateTime = systemCreateTime;
	}

	public String getSystemStatus() {
		return systemStatus;
	}

	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
	}

}
