package com.ifarm.bean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ifarm.annotation.TransientField;

@Entity
@Table(name = "production_device")
public class ProductionDevice {
	@Id
	private Integer deviceId;
	private String deviceVerification;
	private Timestamp createTime;
	private String deviceName;
	private String deviceType;
	private String deviceTypeCode;
	private String deviceVersion;
	private String deviceCode;
	private String deviceDescription;

	@Transient
	@TransientField
	private static transient SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceVerification() {
		return deviceVerification;
	}

	public void setDeviceVerification(String deviceVerification) {
		this.deviceVerification = deviceVerification;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public String getDeviceDescription() {
		return deviceDescription;
	}

	public void setDeviceDescription(String deviceDescription) {
		this.deviceDescription = deviceDescription;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}

	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}

	public ProductionDevice() {

	}

	public ProductionDevice(ProductionDevice productionDevice) {
		this.deviceName = productionDevice.getDeviceName();
		this.deviceType = productionDevice.getDeviceType();
		this.deviceTypeCode = productionDevice.getDeviceTypeCode();
		this.deviceVersion = productionDevice.getDeviceVersion();
		this.deviceDescription = productionDevice.getDeviceDescription();
		this.deviceCode = productionDevice.getDeviceCode();
		this.createTime = Timestamp.valueOf(formatter.format(new Date()));
	}
}
