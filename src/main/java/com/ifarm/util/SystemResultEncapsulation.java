package com.ifarm.util;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.enums.SystemReturnCodeEnum;

public class SystemResultEncapsulation {

	public static String resultCodeDecorate(String resultCode) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", resultCode);
		return jsonObject.toString();
	}

	public static String fillResultCode(
			SystemReturnCodeEnum systemReturnCodeEnum) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", systemReturnCodeEnum.getEngishName());
		jsonObject.put("code", systemReturnCodeEnum.getCode());
		jsonObject.put("description", systemReturnCodeEnum.getDescription());
		return jsonObject.toString();
	}

	public static String fillResultCode(
			SystemReturnCodeEnum systemReturnCodeEnum, String response,
			String description) {
		JSONObject jsonObject = new JSONObject();
		if (response == null) {
			jsonObject.put("response", systemReturnCodeEnum.getEngishName());
		} else {
			jsonObject.put("response", response);
		}
		jsonObject.put("code", systemReturnCodeEnum.getCode());
		if (description == null) {
			jsonObject
					.put("description", systemReturnCodeEnum.getDescription());
		} else {
			jsonObject.put("description", description);

		}
		return jsonObject.toString();
	}

	public static String fillResultCode(
			SystemReturnCodeEnum systemReturnCodeEnum, String extInfo) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", systemReturnCodeEnum.getEngishName());
		jsonObject.put("code", systemReturnCodeEnum.getCode());
		jsonObject.put("description", systemReturnCodeEnum.getDescription());
		jsonObject.put("extInfo", extInfo);
		return jsonObject.toString();
	}

	public static String fillErrorCode(Exception exception) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response",
				SystemReturnCodeEnum.UNKOWN_ERROR.getEngishName());
		jsonObject.put("code", SystemReturnCodeEnum.UNKOWN_ERROR.getCode());
		jsonObject.put("description", exception.getMessage());
		return jsonObject.toString();
	}

	public static String resultTokenDecorate(String resultCode, String token) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("response", resultCode);
		jsonObject.put("token", token);
		return jsonObject.toString();
	}
}
