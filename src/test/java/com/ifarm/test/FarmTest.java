package com.ifarm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.bean.Farm;
import com.ifarm.service.FarmService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class FarmTest {
	@Autowired
	private FarmService farmService;

	@Test
	public void saveFarmTest() {
		/*
		 * Farm farm = new Farm(); farm.setUserId("15025316896");
		 * farm.setFarmName("开心农场"); farm.setFarmLocation("重庆市沙坪坝区");
		 * farm.setFarmDetailAddress("沙正街174号");
		 * farm.setFarmDescribe("土豆、蘑菇、白菜、西红柿"); farm.setFarmState("未激活");
		 * System.out.println(farmService.saveFarm(farm));
		 */
	}

	@Test
	public void updateFarmTest() {
		/*
		 * Farm farm = new Farm(); farm.setFarmId(10000001);
		 * farm.setFarmLocation("重庆市长寿区");
		 * System.out.println(farmService.updateFarm(farm));
		 */
	}

	@Test
	public void getFarms() {
		Farm farm = new Farm();
		farm.setUserId("15025316896");
		System.out.println(farmService.getFarmsList(farm.getUserId()));
	}
}
