package com.ifarm.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ifarm.util.CacheDataBase;

public class CollectorConfigCommand {
	private String collectorId;
	private String data;
	private String sensor;
	private String ipConfig;
	private String time;
	private String stop;
	private boolean addSensor;
	private boolean config;

	public String getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(String collectorId) {
		this.collectorId = collectorId;
	}

	public boolean isAddSensor() {
		return addSensor;
	}

	public void setAddSensor(boolean isAddSensor) {
		this.addSensor = isAddSensor;
	}

	public boolean isConfig() {
		return config;
	}

	public void setConfig(boolean isConfig) {
		this.config = isConfig;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getIpConfig() {
		return ipConfig;
	}

	public void setIpConfig(String ipConfig) {
		this.ipConfig = ipConfig;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public CollectorConfigCommand() {
		this.data = CacheDataBase.configData;
		this.stop = CacheDataBase.configStop;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTimeString = simpleDateFormat.format(new Date());
		this.time = CacheDataBase.configTime.replace("?", nowTimeString.replace(" ", ","));
		this.addSensor = false;
		this.config = false;
	}

}
