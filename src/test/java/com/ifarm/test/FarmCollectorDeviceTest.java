package com.ifarm.test;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.ifarm.service.FarmCollectorDeviceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class FarmCollectorDeviceTest {
	
	@Autowired
	private FarmCollectorDeviceService farmCollectorDeviceService;
	
	@Test
	public void test() {
		Map<String, JSONArray> map = farmCollectorDeviceService.queryCollectorDeviceType(10000001);
		for (String key : map.keySet()) {
			System.out.println(key);
			System.out.println(map.get(key));
		}
	}
}
