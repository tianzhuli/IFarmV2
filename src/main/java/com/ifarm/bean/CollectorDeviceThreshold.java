package com.ifarm.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "collector_device_threshold")
public class CollectorDeviceThreshold {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer thresholdId;
	private Long deviceId;
	private Integer illuminationUpperThreshold;
	private Integer illuminationDownThreshold;
	private Double airTemperatureUpperThreshold;
	private Double airTemperatureDownThreshold;
	private Double airHumidityUpperThreshold;
	private Double airHumidityDownThreshold;
	private Double soilTemperatureUpperThreshold;
	private Double soilTemperatureDownThreshold;
	private Double soilHumidityUpperThreshold;
	private Double soilHumidityDownThreshold;

	public Integer getThresholdId() {
		return thresholdId;
	}

	public void setThresholdId(Integer thresholdId) {
		this.thresholdId = thresholdId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getIlluminationUpperThreshold() {
		return illuminationUpperThreshold;
	}

	public void setIlluminationUpperThreshold(Integer illuminationUpperThreshold) {
		this.illuminationUpperThreshold = illuminationUpperThreshold;
	}

	public Integer getIlluminationDownThreshold() {
		return illuminationDownThreshold;
	}

	public void setIlluminationDownThreshold(Integer illuminationDownThreshold) {
		this.illuminationDownThreshold = illuminationDownThreshold;
	}

	public Double getAirTemperatureUpperThreshold() {
		return airTemperatureUpperThreshold;
	}

	public void setAirTemperatureUpperThreshold(Double airTemperatureUpperThreshold) {
		this.airTemperatureUpperThreshold = airTemperatureUpperThreshold;
	}

	public Double getAirTemperatureDownThreshold() {
		return airTemperatureDownThreshold;
	}

	public void setAirTemperatureDownThreshold(Double airTemperatureDownThreshold) {
		this.airTemperatureDownThreshold = airTemperatureDownThreshold;
	}

	public Double getAirHumidityUpperThreshold() {
		return airHumidityUpperThreshold;
	}

	public void setAirHumidityUpperThreshold(Double airHumidityUpperThreshold) {
		this.airHumidityUpperThreshold = airHumidityUpperThreshold;
	}

	public Double getAirHumidityDownThreshold() {
		return airHumidityDownThreshold;
	}

	public void setAirHumidityDownThreshold(Double airHumidityDownThreshold) {
		this.airHumidityDownThreshold = airHumidityDownThreshold;
	}

	public Double getSoilTemperatureUpperThreshold() {
		return soilTemperatureUpperThreshold;
	}

	public void setSoilTemperatureUpperThreshold(Double soilTemperatureUpperThreshold) {
		this.soilTemperatureUpperThreshold = soilTemperatureUpperThreshold;
	}

	public Double getSoilTemperatureDownThreshold() {
		return soilTemperatureDownThreshold;
	}

	public void setSoilTemperatureDownThreshold(Double soilTemperatureDownThreshold) {
		this.soilTemperatureDownThreshold = soilTemperatureDownThreshold;
	}

	public Double getSoilHumidityUpperThreshold() {
		return soilHumidityUpperThreshold;
	}

	public void setSoilHumidityUpperThreshold(Double soilHumidityUpperThreshold) {
		this.soilHumidityUpperThreshold = soilHumidityUpperThreshold;
	}

	public Double getSoilHumidityDownThreshold() {
		return soilHumidityDownThreshold;
	}

	public void setSoilHumidityDownThreshold(Double soilHumidityDownThreshold) {
		this.soilHumidityDownThreshold = soilHumidityDownThreshold;
	}

}
