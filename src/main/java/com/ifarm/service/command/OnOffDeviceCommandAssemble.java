package com.ifarm.service.command;

import org.springframework.stereotype.Component;

import com.ifarm.bean.ControlCommand;
import com.ifarm.util.ByteConvert;

/**
 * 通断控制器
 * @author qican
 *
 */
@Component
public class OnOffDeviceCommandAssemble extends CommandAssemble{

	@Override
	public byte[] customAssemble(ControlCommand command) {
		// TODO Auto-generated method stub
		byte[] arr = new byte[11];
		byte[] collectorArray = ByteConvert.convertTobyte(String.valueOf(command.getControlDeviceId()), false);
		arr[0] = 0x03; // 设备类型标识，目前都是对应的通断器
		for (int i = 0; i < collectorArray.length; i++) {
			arr[i + 1] = collectorArray[i];
		}
		arr[5] = 0x01; // 主功能号
		arr[6] = 0x00; // 次功能号
		// 01置位，02关掉，03开启
		switch (command.getCommandCategory()) {
		case "execution":
			arr[7] = 0x03;
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
		byte[] bits = ByteConvert.terminalBitsConvert(command.getControlTerminalbits(), 2);
		arr[9] = bits[0];
		arr[10] = bits[1];
		return arr;
	}
}
