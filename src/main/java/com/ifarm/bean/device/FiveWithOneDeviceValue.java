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
@Table(name = "collector_device_value_five_with_one")
public class FiveWithOneDeviceValue extends DeviceValueBase {
	/**
	 * 
	 */
	@Transient
	@TransientField
	private static final long serialVersionUID = -3614143073913290853L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TransientField
	private Integer deviceValueId;
	private Long deviceId;
	private Double illumination;
	private Double carbonDioxide;
	private Double oxygen;
	private Double airTemperature;
	private Double airHumidity;
	private Timestamp updateTime;
	@Transient
	transient private byte[] rawPacket;

	/**
	 * @param size
	 *            数据包大小，目前暂无用处
	 * @param arr
	 *            数据包
	 */
	public void setCollectData(byte[] arr, int pos) {

		/*
		 * this.illumination = convertData.getdataType3(arr, 0) * 1.0;
		 * this.airTemperature = convertData.getdataType3(arr, 2) * 1.00 / 100;
		 * this.airHumidity = convertData.getdataType3(arr, 4) * 1.00 / 100;
		 * this.airHumidity = this.airHumidity >= 100 ? 99.99 :
		 * this.airHumidity; this.soilTemperature =
		 * convertData.getdataType3(arr, 6) * 1.00 / 100; this.soilHumidity =
		 * convertData.getdataType3(arr, 8) * 1.00 / 100; this.soilHumidity =
		 * this.soilHumidity >= 100 ? 99.99 : this.soilHumidity;
		 */
		this.illumination = convertData.getdataType5(arr, pos += 4) * 1.0;
		this.carbonDioxide = convertData.getdataType3(arr, pos += 4) * 1.0;
		this.oxygen = convertData.getdataType3(arr, pos += 2) * 1.0 / 100;
		this.airTemperature = convertData.getdataType3(arr, pos += 2) * 1.00 / 100;
		this.airHumidity = convertData.getdataType3(arr, pos += 2) * 1.00 / 100;
		this.airHumidity = this.airHumidity >= 100 ? 99.99 : this.airHumidity;
		rawPacket = arr;
	}

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

	public Double getIllumination() {
		return illumination;
	}

	public void setIllumination(Double illumination) {
		this.illumination = illumination;
	}

	public Double getCarbonDioxide() {
		return carbonDioxide;
	}

	public void setCarbonDioxide(Double carbonDioxide) {
		this.carbonDioxide = carbonDioxide;
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
	public DeviceValueType getDeviceValueType() {
		// TODO Auto-generated method stub
		return DeviceValueType.FIVE_WITH_ONE;
	}

}
