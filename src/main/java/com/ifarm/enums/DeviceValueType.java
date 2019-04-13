package com.ifarm.enums;

public enum DeviceValueType {

	FIVE_WITH_ONE("five_with_one", "五和一", "光照强度,二氧化碳,氧气,空气温度,空气湿度", "illumination,carbonDioxide,oxygen,airTemperature,airHumidity", "Lx,ppm,%,℃,%"), 
	SEVEN_WITH_ONE("seven_with_one", "七和一", "光照强度,二氧化碳,氧气,空气温度,空气湿度,土壤温度,土壤湿度","illumination,carbonDioxide,oxygen,airTemperature,airHumidity,soilTemperature,soilHumidity","Lx,ppm,%,℃,%,℃,%"),
	COLLECOT_TYPE_FIVE("collectorType5","老版五和一", "光照强度,空气温度,空气湿度,土壤温度,土壤湿度", "illumination,airTemperature,airHumidity,soilTemperature,soilHumidity", "Lx,℃,%,℃,%"),
	Air_TEMHUM("airTemHum","空气温湿度", "空气温度,空气湿度", "airTemperature,airHumidity", "℃,%"), 
	CO2("co2", "二氧化碳", "二氧化碳", "carbonDioxide", "ppm"),
	OXYGEN("oxygen", "氧气", "氧气", "oxygen", "%"), 
	ILLUMINATION("illuminition", "光照强度", "光照强度", "illuminition", "Lx"), 
	SOIL_TEMHUM("soilTemHum", "土壤温湿度", "土壤温度,土壤湿度", "soilTemperature,soilHumidity", "℃,%"),
	WEATHER_MONITOR("weatherMonitor", "气象监测", "风速,风向,雨量", "windSpeed,windDirection,rainFall", "m,°,mm");
	
	private String code;
	private String ChineseName;
	private String param;
	private String paramCode;
	private String paramUnit;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getChineseName() {
		return ChineseName;
	}

	public void setChineseName(String chineseName) {
		ChineseName = chineseName;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getParamCode() {
		return paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	
	public String getParamUnit() {
		return paramUnit;
	}

	public void setParamUnit(String paramUnit) {
		this.paramUnit = paramUnit;
	}

	private DeviceValueType(String code, String ChineseName, String param, String paramCode, String unit) {
		this.code = code;
		this.ChineseName = ChineseName;
		this.param = param;
		this.paramCode = paramCode;
		this.paramUnit = unit;
	}
	
	public static DeviceValueType getValueTypeByCode(String code) {
		for (DeviceValueType deviceValueType: DeviceValueType.values()) {
			if (deviceValueType.getCode().equals(code)) {
				return deviceValueType;
			}
		}
		return null;
	}
}
