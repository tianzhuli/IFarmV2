package com.ifarm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UserTest {
	@Autowired
	private UserService userService;

	// @Test
	// public void test() {
	// /*
	// * String pwd = "123"; String base64 =
	// * Base64.encodeBase64String(pwd.getBytes());
	// * System.out.println(base64); String basePwd = new
	// * String(Base64.decodeBase64(base64)); System.out.println(basePwd);
	// */
	// }
	//
	// @Test
	// public void userResgiterTest() {
	// /*
	// * User user = new User(); user.setUserId("18795632458");
	// * user.setUserPwd("123456");
	// * System.out.println(userService.userResgiter(user));
	// */
	// }
	//
	// @Test
	// public void userLoginTest() {
	// /*
	// * User user = new User(); user.setUserId("18795632458"); String base64
	// * = Base64.encodeBase64String("123456".getBytes()); //
	// * System.out.println(base64); user.setUserPwd(base64);
	// * System.out.println(userService.userLogin(user,
	// * userService.userGetToken(user.getUserId())));
	// */
	// }
	//
	// @Test
	// public void updateUserTest() {
	// /*
	// * User user = new User(); user.setUserId("18795632458");
	// * user.setUserPwd("123");
	// * System.out.println(userService.updateUser(user));
	// */
	// }
	//
	// @Test
	// public void getUsersListAroundTest() {
	// String userId = "";
	// Page page = new Page();
	// page.setBeginIndex(0);
	// page.setCount(3);
	// System.out.println(userService.getUsersListAround(userId, page));
	// }

	@Test
	public void addSubUserTest() {
		String userId = "00000000000";
		Integer farmId = 10000001;
		String authority = "onlySee";
		String res = userService.addSubUser(userId, farmId, authority);
		System.out.println("res:" + res);
	}
}
