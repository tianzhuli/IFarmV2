package com.ifarm.test;

import java.util.Iterator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ControlSystemTest {
	public static void main(String[] args) {
		String json = "{\"vavleControl\":\"vavle:阀门控制系统\",\"carbonDioxideControl\":\"carbonDioxide:二氧化碳控制系统\",\"sideRollerShuttersControl\":\"rollerShutters:侧卷帘系统\",\"waterRollerShuttersControl\":\"waterRollerShutters:水帘控制系统\",\"humidityControl\":\"humidity:湿度控制系统\",\"oxygenControl\":\"oxygen:氧气控制系统\",\"topRollerShuttersControl\":\"rollerShutters:顶卷帘系统\",\"temperatureControl\":\"temperature:温度控制系统\",\"sunshadeControl\":\"sunshade:遮阳控制系统\",\"fillLightControl\":\"supplementaryLighting:补光控制系统\",\"draughtFanControl\":\"ventilate:通风控制系统\"}";
		JSONObject jsonObject = JSON.parseObject(json);
		Iterator<String> iterator = jsonObject.keySet().iterator();
		JSONArray jsonArray = new JSONArray();
		while (iterator.hasNext()) {
			JSONObject jsonObject2 = new JSONObject();
			String key = iterator.next();
			String[] values  = jsonObject.getString(key).split(":");
			jsonObject2.put("systemCode", key);
			jsonObject2.put("systemType", values[0]);
			jsonObject2.put("systemTypeCode", values[1]);
			jsonArray.add(jsonObject2);
		}
		System.out.println(jsonArray);
	}
}
