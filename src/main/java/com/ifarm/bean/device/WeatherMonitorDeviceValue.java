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
@Table(name = "collector_device_value_weather_monitor")
public class WeatherMonitorDeviceValue extends DeviceValueBase {
	
	/**
	 * UID
	 */
	@Transient
	@TransientField
	private static final long serialVersionUID = 1729379982818182L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TransientField
	private Integer deviceValueId;
	private Long deviceId;
	private Double windSpeed;
	private Double windDirection;
	private Double rainFall;
	private Timestamp updateTime;
	@Transient
	transient private byte[] rawPacket;
	
	@Override
	public void setCollectData(byte[] arr, int size) {
		// TODO Auto-generated method stub
		this.windSpeed = convertData.getdataType3(arr, size += 4) * 1.0 / 10;
		this.windDirection = convertData.getdataType3(arr, size += 2) * 1.0;
		this.rainFall = convertData.getdataType3(arr, size += 2) * 1.0 / 10;
		rawPacket = arr;
	}

	@Override
	public Timestamp getUpdateTime() {
		// TODO Auto-generated method stub
		return updateTime;
	}

	@Override
	public void setUpdateTime(Timestamp valueOf) {
		// TODO Auto-generated method stub
		this.updateTime = valueOf;
	}

	@Override
	public void setDeviceId(Long id) {
		// TODO Auto-generated method stub
		this.deviceId = id;
	}

	@Override
	public Long getDeviceId() {
		// TODO Auto-generated method stub
		return deviceId;
	}

	@Override
	public DeviceValueType getDeviceValueType() {
		// TODO Auto-generated method stub
		return DeviceValueType.WEATHER_MONITOR;
	}

	public Double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Double getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}

	public Double getRainFall() {
		return rainFall;
	}

	public void setRainFall(Double rainFall) {
		this.rainFall = rainFall;
	}

}
