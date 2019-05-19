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
@Table(name = "collector_device_value_o2")
public class OxygenDeviceValue extends DeviceValueBase {
	/**
	 * 
	 */
	@Transient 
	@TransientField
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TransientField
	private Integer deviceValueId;
	private Long deviceId;
	private Double oxygen;
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

	public Double getOxygen() {
		return oxygen;
	}

	public void setOxygen(Double oxygen) {
		this.oxygen = oxygen;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public void setCollectData(byte[] arr, int pos) {
		// TODO Auto-generated method stub
		this.oxygen = convertData.getdataType3(arr, pos += 2) * 1.0 / 100;
		rawPacket = arr;
	}

	@Override
	public DeviceValueType getDeviceValueType() {
		// TODO Auto-generated method stub
		return DeviceValueType.OXYGEN;
	}

}
