package com.ifarm.util;

import com.alibaba.fastjson.JSONObject;

public class SystemResultEncapsulation {

	public static String resultCodeDecorate(String resultCode) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", resultCode);
		return jsonObject.toString();
	}

	public static String resultTokenDecorate(String resultCode,String token) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", resultCode);
		jsonObject.put("token", token);
		return jsonObject.toString();
	}
}
