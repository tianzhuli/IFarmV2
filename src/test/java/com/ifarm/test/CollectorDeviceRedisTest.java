package com.ifarm.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.redis.util.CollectorDeviceValueRedisUtil;
import com.ifarm.util.JsonObjectUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class CollectorDeviceRedisTest {
	@Autowired
	private CollectorDeviceValueRedisUtil redisUtil;

	@Test
	public void getCollectorDeviceValueTest() {
		List<DeviceValueBase> list = redisUtil.getCollectorDeviceCacheValue(new Long(10000001));
		for (int i = 0; i < list.size(); i++) {
			DeviceValueBase deviceValueBase = list.get(i);
			System.out.println(JsonObjectUtil.toJsonObject(deviceValueBase));
		}
	}
}
