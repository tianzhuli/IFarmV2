package com.ifarm.test;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.redis.util.FarmControlSystemTypeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class FarmControlSystemTypeTest {
	@Autowired
	private FarmControlSystemTypeUtil farmControlSystemTypeUtil;
	
	@Test
	public void test() {
		System.out.println(farmControlSystemTypeUtil.farmControlSystemType());
	}
}
