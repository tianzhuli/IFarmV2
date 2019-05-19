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
@Table(name = "collector_device_value_air_temHum")
public class AirTemHumDeviceValue extends DeviceValueBase {
	/**
	 * 
	 */
	@Transient
	@TransientField
	private static final long serialVersionUID = 4849931912677018801L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TransientField
	private Integer deviceValueId;
	private Long deviceId;
	private Double airTemperature;
	private Double airHumidity;

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

	public Double getAirTemperature() {
		return airTemperature;
	}

	public void setAirTemperature(Double airTemperature) {
		this.airTemperature = airTemperature;
	}

	public Double getAirHumidity() {
		return airHumidity;
	}

	public void setAirHumidity(Double airHumidity) {
		this.airHumidity = airHumidity;
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

	private Timestamp updateTime;
	@Transient
	transient private byte[] rawPacket;

	@Override
	public void setCollectData(byte[] arr, int pos) {
		// TODO Auto-generated method stub
		this.airTemperature = convertData.getdataType3(arr, pos) * 1.00 / 100;
		this.airHumidity = convertData.getdataType3(arr, pos + 2) * 1.00 / 100;
		this.airHumidity = this.airHumidity >= 100 ? 99.99 : this.airHumidity;
		this.rawPacket = arr;
	}

	public AirTemHumDeviceValue(Long deviceId, byte[] arr) {
		this.deviceId = deviceId;
		setCollectData(arr, 12);
	}
	
	public AirTemHumDeviceValue() {
		
	}

	@Override
	public DeviceValueType getDeviceValueType() {
		// TODO Auto-generated method stub
		return DeviceValueType.Air_TEMHUM;
	}
}
