package com.ifarm.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class JSONArrayTest {
	public static void main(String[] args) {
		JSONArray array = new JSONArray();
		update(array);
		System.out.println(array);
		try {
			JSONObject jsonObject = JSONObject.parseObject(array.toString());
			System.out.println(jsonObject);
		} catch (JSONException e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}	
	}
	
	public static void update(JSONArray array) {
		array.add("123");
	}
}
