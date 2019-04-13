package com.ifarm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.service.FarmControlSystemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class FarmControlSystemTest {
	@Autowired
	private FarmControlSystemService farmControlSystemService;
	
	@Test
	public void testRegion() {
		System.out.println(farmControlSystemService.controlSystemRegion("10000026"));
	}
	
	@Test
	public void regionSystemTest() {
		System.out.println(farmControlSystemService.regionControlSystem("10000003","A区", "1号"));
	}
}
