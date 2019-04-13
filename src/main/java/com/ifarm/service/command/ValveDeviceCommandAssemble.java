package com.ifarm.service.command;

import org.springframework.stereotype.Component;

import com.ifarm.bean.ControlCommand;
import com.ifarm.util.ByteConvert;

@Component
public class ValveDeviceCommandAssemble extends CommandAssemble {

	@Override
	public byte[] customAssemble(ControlCommand command) {
		// TODO Auto-generated method stub
		byte[] arr = new byte[9];
		byte[] collectorArray = ByteConvert.convertTobyte(String.valueOf(command.getControlDeviceId()), false);
		arr[0] = 0x04; // 设备类型标识
		for (int i = 0; i < collectorArray.length; i++) {
			arr[i + 1] = collectorArray[i];
		}
		arr[5] = 0x01; // 主功能号
		arr[6] = 0x00; // 次功能号
		// 01置位，02关掉，03开启
		switch (command.getCommandCategory()) {
		case "execution":
			arr[7] = 0x01;
			arr[8] = 0x00;
			break;
		case "stop":
			arr[7] = 0x02;
			arr[8] = 0x00;
			break;
		default:
			arr[7] = 0x00;
			arr[8] = 0x00; //默认可以做查询
			break;
		}
		return arr;
	}

}
