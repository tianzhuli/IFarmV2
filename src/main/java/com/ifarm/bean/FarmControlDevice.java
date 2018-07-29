package com.ifarm.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "farm_control_device")
public class FarmControlDevice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer controlDeviceId;
	private Long collectorId;
	private String deviceType;
	private String deviceLocation;
	private String deviceDescription;
	private Timestamp deviceProduceTime;

	public Integer getControlDeviceId() {
		return controlDeviceId;
	}

	public void setControlDeviceId(Integer controlDeviceId) {
		this.controlDeviceId = controlDeviceId;
	}

	public Long getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceLocation() {
		return deviceLocation;
	}

	public void setDeviceLocation(String deviceLocation) {
		this.deviceLocation = deviceLocation;
	}

	public String getDeviceDescription() {
		return deviceDescription;
	}

	public void setDeviceDescription(String deviceDescription) {
		this.deviceDescription = deviceDescription;
	}

	public Timestamp getDeviceProduceTime() {
		return deviceProduceTime;
	}

	public void setDeviceProduceTime(Timestamp deviceProduceTime) {
		this.deviceProduceTime = deviceProduceTime;
	}

}
