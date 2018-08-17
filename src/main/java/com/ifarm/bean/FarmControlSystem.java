package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ifarm.annotation.LikeField;

@Entity
@Table(name = "farm_control_system")
public class FarmControlSystem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer systemId;
	private Integer farmId;
	private String systemCode;
	@LikeField
	private String systemType;
	private String systemTypeCode;
	@LikeField
	private String systemDistrict;
	@LikeField
	private String systemNo;
	@LikeField
	private String systemDescription;
	@LikeField
	private String systemLocation;
	private Timestamp systemCreateTime;

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Integer getSystemId() {
		return systemId;
	}

	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

	public Integer getFarmId() {
		return farmId;
	}

	public String getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getSystemType() {
		return systemType;
	}

	public String getSystemTypeCode() {
		return systemTypeCode;
	}

	public void setSystemTypeCode(String systemTypeCode) {
		this.systemTypeCode = systemTypeCode;
	}

	public void setSystemDistrict(String systemDistrict) {
		this.systemDistrict = systemDistrict;
	}

	public String getSystemDistrict() {
		return systemDistrict;
	}

	public void setSystemLocation(String systemLocation) {
		this.systemLocation = systemLocation;
	}

	public String getSystemLocation() {
		return systemLocation;
	}

	public String getSystemDescription() {
		return systemDescription;
	}

	public void setSystemDescription(String systemDescription) {
		this.systemDescription = systemDescription;
	}

	public void setSystemCreateTime(Timestamp systemCreateTime) {
		this.systemCreateTime = systemCreateTime;
	}

	public Timestamp getSystemCreateTime() {
		return systemCreateTime;
	}

}
