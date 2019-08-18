package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ifarm.annotation.Validator;

@Entity
@Table(name = "farm_collector_device")
public class FarmCollectorDevice {
	@Id
	private Long deviceId;
	private Integer farmId;
	private Long collectorId;
	private Integer collectorSerialNo;
	@Validator
	private Integer unitNo;
	private Integer deviceOrderNo;
	private String deviceVersion;
	private String deviceType;
	private String deviceDistrict;
	private String deviceLocation;
	private Timestamp deviceCreateTime;
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getFarmId() {
		return farmId;
	}
	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}
	public Long getCollectorId() {
		return collectorId;
	}

	public Integer getCollectorSerialNo() {
		return collectorSerialNo;
	}

	public void setCollectorSerialNo(Integer collectorSerialNo) {
		this.collectorSerialNo = collectorSerialNo;
	}

	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}
	public Integer getUnitNo() {
		return unitNo;
	}
	public void setUnitNo(Integer unitNo) {
		this.unitNo = unitNo;
	}
	public Integer getDeviceOrderNo() {
		return deviceOrderNo;
	}
	public void setDeviceOrderNo(Integer deviceOrderNo) {
		this.deviceOrderNo = deviceOrderNo;
	}
	public String getDeviceVersion() {
		return deviceVersion;
	}
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceDistrict() {
		return deviceDistrict;
	}
	public void setDeviceDistrict(String deviceDistrict) {
		this.deviceDistrict = deviceDistrict;
	}
	public String getDeviceLocation() {
		return deviceLocation;
	}
	public void setDeviceLocation(String deviceLocation) {
		this.deviceLocation = deviceLocation;
	}
	public Timestamp getDeviceCreateTime() {
		return deviceCreateTime;
	}
	public void setDeviceCreateTime(Timestamp deviceCreateTime) {
		this.deviceCreateTime = deviceCreateTime;
	}

	

}
