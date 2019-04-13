package com.ifarm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.bean.ControlCommand;
import com.ifarm.service.CommandConvertService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class CommandConvertServiceTest {
	
	@Autowired
	private CommandConvertService commandConvertService;
	
	@Test
	public void test() {
		ControlCommand command = new ControlCommand();
		command.setCommandCategory("execution");
		command.setControlDeviceId(14916569);
		int[] bits = new int[16];
		bits[0] = 1;
		command.setControlTerminalbits(bits);
		byte[] arr = commandConvertService.commandToByte(command);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(Integer.toHexString(arr[i] & 0xFF));
		}
	}
}
