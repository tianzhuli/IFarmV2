package com.ifarm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.nosql.dao.UserLogMongoDao;
import com.ifarm.service.ManagerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class ManagerTest {

	@Autowired
	private ManagerService managerService;
	@Autowired
	private UserLogMongoDao uDao;

	@Test
	public void getManagerByIdTest() {
		/*
		 * String managerId = "2011"; Manager manager =
		 * managerService.getManagerById(managerId);
		 * ReflectTraverse.traverseObject(manager);
		 */
	}

	@Test
	public void getAllManagerTest() {

	}

	@Test
	public void updateManagerTest() {
		/*
		 * Manager manager = new Manager(); manager.setManagerId("2011");
		 * manager.setManagerPwd("123");
		 * System.out.println(managerService.updateManager(manager));
		 */
	}

	@Test
	public void getManager() {
		/*
		 * Manager manager = new Manager(); manager.setManagerPwd("123"); String
		 * managers = managerService.getManager(manager);
		 * System.out.println(managers);
		 */
		/*
		 * String begin = "2017-01-16 17:19:46"; String end =
		 * "2017-01-16 17:19:49";
		 * System.out.println(uDao.getUserLogByTime(begin, end));
		 */
	}
}
