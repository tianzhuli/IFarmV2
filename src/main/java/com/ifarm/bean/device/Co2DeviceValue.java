package com.ifarm.bean.device;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ifarm.annotation.TransientField;
import com.ifarm.bean.DeviceValueBase;
import com.ifarm.enums.DeviceValueType;
@Entity
@Table(name = "collector_device_value_co2")
public class Co2DeviceValue extends DeviceValueBase {
	/**
	 * 
	 */
	@Transient
	@TransientField
	private static final long serialVersionUID = 2237623923378991247L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TransientField
	private Integer deviceValueId;
	private Long deviceId;
	private Double carbonDioxide;
	private Timestamp updateTime;
	@Transient
	transient private byte[] rawPacket;

	public Integer getDeviceValueId() {
		return deviceValueId;
	}

	public void setDeviceValueId(Integer deviceValueId) {
		this.deviceValueId = deviceValueId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Double getCarbonDioxide() {
		return carbonDioxide;
	}

	public void setCarbonDioxide(Double carbonDioxide) {
		this.carbonDioxide = carbonDioxide;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public byte[] getRawPacket() {
		return rawPacket;
	}

	public void setRawPacket(byte[] rawPacket) {
		this.rawPacket = rawPacket;
	}

	@Override
	public void setCollectData(byte[] arr, int pos) {
		// TODO Auto-generated method stub
		this.carbonDioxide = convertData.getdataType3(arr, pos += 2) * 1.0;
		rawPacket = arr;
	}

	@Override
	public DeviceValueType getDeviceValueType() {
		// TODO Auto-generated method stub
		return DeviceValueType.CO2;
	}

}
