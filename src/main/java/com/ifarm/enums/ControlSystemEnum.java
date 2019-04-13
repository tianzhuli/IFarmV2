package com.ifarm.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public enum ControlSystemEnum {
	TEMPERATURE("temperature", "温度控制系统", "温度", "temperatureControl"),
	HUMIDITY("humidity", "湿度控制系统", "湿度", "humidityControl"),
	TOP_ROLLER_SHUTTER("topRollerShutter", "顶卷帘控制系统", "顶卷帘", "topRollerShutterControl"),
	SIDE_ROLLER_SHUTTER("sideRollerShutter", "侧卷帘控制系统", "侧卷帘", "sideRollerShutterControl"),
	WATER_ROLLER_SHUTTER("waterRollerShutter", "水卷帘控制系统", "水卷帘", "waterRollerShutterControl"),
	VENTILATE("ventilate", "通风控制系统", "通风", "draughtFanControl"),
	FILLLIGHT("fillLight", "补光控制系统", "补光", "fillLightControl"),
	CARBONDIOXIDE("carbonDioxide", "二氧化碳控制系统", "二氧化碳", "carbonDioxideControl"),
	OXYGEN("oxygen", "氧气控制系统", "氧气", "oxygenControl"),
	SUNSHADE("sunshade", "遮阳控制系统", "遮阳", "sunshadeControl"),
	VALVE("valve", "阀门控制系统", "阀门", "valveControl"),
	WATER_FERTILIZER_MEDICINDE("waterFertilizerMedicine", "水肥药系统", "水肥药", "waterFertilizerMedicineControl");
	
	private String code;
	private String ChineseName;
	private String shortName;
	private String type;

	private ControlSystemEnum(String code, String ChineseName, String shortName, String type) {
		this.code = code;
		this.ChineseName = ChineseName;
		this.shortName = shortName;
		this.type = type;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public static ControlSystemEnum getValueByCode(String code) {
		for (ControlSystemEnum controlSystemEnum: ControlSystemEnum.values()) {
			if (controlSystemEnum.getCode().equals(code)) {
				return controlSystemEnum;
			}
		}
		return null;
	}
	
	public static ControlSystemEnum getValueByType(String type) {
		for (ControlSystemEnum controlSystemEnum: ControlSystemEnum.values()) {
			if (controlSystemEnum.getType().equals(type)) {
				return controlSystemEnum;
			}
		}
		return null;
	}
	
	public static String toJSONString() {
		JSONArray jsonArray = new JSONArray();
		for(ControlSystemEnum controlSystemEnum: ControlSystemEnum.values()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", controlSystemEnum.getCode());
			jsonObject.put("name", controlSystemEnum.getChineseName());
			jsonArray.add(jsonObject);
		}
		return jsonArray.toJSONString();
	}

}