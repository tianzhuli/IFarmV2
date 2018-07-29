package com.ifarm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.service.FarmControlSystemService;
import com.ifarm.service.CollectorDeviceValueService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class SensorTest {
	@Autowired
	private CollectorDeviceValueService sensorValuesService;

	@Autowired
	private FarmControlSystemService farmControlService;

	/*
	 * @Test public void historySensorTest() { FarmSensor farmSensor = new
	 * FarmSensor(); farmSensor.setSensorId("20160003"); Timestamp beginTime =
	 * Timestamp.valueOf("2017-01-16 15:58:21"); Timestamp endTime =
	 * Timestamp.valueOf("2017-02-24 19:54:57");
	 * System.out.println(sensorValuesService.getHistorySensorValues(farmSensor,
	 * beginTime, endTime)); }
	 */

	/*
	 * @Test public void save() { SensorValue sensorValue = new SensorValue();
	 * sensorValue.setSensorId("20160001"); sensorValue.setSensorValues(1.1);
	 * sensorValue.setUpdateTime(Timestamp.valueOf("2017-01-16 15:58:21"));
	 * sensorValuesService.saveSensorValueByCode(sensorValue, "illumination"); }
	 */

	@Test
	public void test() {
		// System.out.println(farmControlService.farmControlSystemState(10000001,
		// "1号控制系统"));
	}
}
