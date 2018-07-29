package com.ifarm.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ifarm.bean.WFMControlCommand;
import com.ifarm.bean.WFMControlTask;
import com.ifarm.service.FarmControlSystemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class FarmControlTest {
	@Autowired
	private FarmControlSystemService farmControlSystemService;

	@Test
	public void test() {
		WFMControlTask controlTask = new WFMControlTask();
		controlTask.setSystemId(100001);
		controlTask.setControlType("fertilizer");
		controlTask.setControlArea("2");
		controlTask.setCanNo("1,2");
		controlTask.setLevel(2);
		controlTask.setCommandCategory("execution");
		farmControlSystemService.produceWFMControlTaskCommand(controlTask);
		List<WFMControlCommand> commads = controlTask.getWfmControlCommands();
		for (int i = 0; i < commads.size(); i++) {
			WFMControlCommand commad = commads.get(i);
			byte[] arr = commad.commandToByte();
			System.out.println("deviceId:" + commad.getControlDeviceId());
			for (int j = 0; j < arr.length; j++) {
				System.out.print(Integer.toHexString(arr[j] & 0xff) + " ");
			}
			System.out.println();
			commad.setCommandCategory("stop");
			byte[] arr1 = commad.commandToByte();
			System.out.println("indentifying:" + commad.getIndentifying());
			for (int j = 0; j < arr1.length; j++) {
				System.out.print(Integer.toHexString(arr1[j] & 0xff) + " ");
			}
			System.out.println();
		}
	}
}
