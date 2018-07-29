package com.ifarm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.service.FarmControlSystemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class ControlTest {
	@Autowired
	private FarmControlSystemService farmControlService;

	@Test
	public void test() {
		System.out.println("result:" + farmControlService.farmControlSystemVerification("13594039472", 10000001, 17130000));
	}
	
	public void update() {
		
	}
}
