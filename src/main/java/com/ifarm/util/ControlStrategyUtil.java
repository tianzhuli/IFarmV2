package com.ifarm.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ifarm.bean.ControlTask;

/**
 * 控制的具体策略，主要是各种控制类型和操作对应生成不同的bit数组
 * 
 * @author lab
 * 
 */
public class ControlStrategyUtil {
	public static final Map<String, String> controlStrategyDescription = new HashMap<String, String>();
	static {
		// 方便阅读
		controlStrategyDescription.put("topRollerShuttersControl", "顶卷帘控制");
		controlStrategyDescription.put("sideRollerShuttersControl", "侧卷帘控制");
		controlStrategyDescription.put("sunshadeControl", "遮阳控制");
		controlStrategyDescription.put("valveControl", "阀门控制");
		controlStrategyDescription.put("waterRollerShuttersControl", "水卷控制");
		controlStrategyDescription.put("fillLightControl", "补光控制");
		controlStrategyDescription.put("draughtFanControl", "风机控制");
		controlStrategyDescription.put("carbonDioxideControl", "二氧化碳控制");
		controlStrategyDescription.put("oxygenControl", "氧气机控制");
		controlStrategyDescription.put("temperatureControl", "温度控制");
		controlStrategyDescription.put("humidityControl", "湿度控制");
	}

	public static void controlTaskOperation(ControlTask controlTask, List<Object[]> list) throws Exception {
		String controlType = controlTask.getControlType(); // 不同的控制类型业务完全不同
		Method method = ControlStrategyUtil.class.getMethod(controlType, new Class[] { ControlTask.class, List.class });
		method.invoke(null, controlTask, list);
	}

	/**
	 * 顶卷帘的控制,操作就是升和降,rise and fall
	 * 
	 * @param controlTask
	 */
	public static void topRollerShuttersControl(ControlTask controlTask, List<Object[]> list) {
		String controlOperation = controlTask.getControlOperation();
		int[] bits = new int[32];
		int riseBit = 0;
		for (int i = 0; i < list.size(); i++) {
			Object[] farmControlTerminal = list.get(i);
			String functionCode = (String) farmControlTerminal[5];
			if ("enable".equals(functionCode)) {
				riseBit = (int) farmControlTerminal[3];
			}
		}
		if ("enable".equals(controlOperation)) {
			bits[riseBit] = 1;
		}
		controlTask.setControlTerminalbits(bits);
	}

	/**
	 * 侧卷帘的控制
	 * 
	 * @param controlTask
	 */
	public static void sideRollerShuttersControl(ControlTask controlTask, List<Object[]> list) {
		String controlOperation = controlTask.getControlOperation();
		int[] bits = new int[32];
		int enableBit = 0;
		for (int i = 0; i < list.size(); i++) {
			Object[] farmControlTerminal = list.get(i);
			String functionCode = (String) farmControlTerminal[5];
			if ("enable".equals(functionCode)) {
				enableBit = (int) farmControlTerminal[3];
			}
		}
		if ("enable".equals(controlOperation)) {
			bits[enableBit] = 1;
		}
		controlTask.setControlTerminalbits(bits);
	}

	/**
	 * 遮阳控制
	 * 
	 * @param controlTask
	 */
	public static void sunshadeControl(ControlTask controlTask, List<Object[]> list) {
		String controlOperation = controlTask.getControlOperation();
		int[] bits = new int[32];
		int enableBit = 0;
		for (int i = 0; i < list.size(); i++) {
			Object[] farmControlTerminal = list.get(i);
			String functionCode = (String) farmControlTerminal[5];
			if ("enable".equals(functionCode)) {
				enableBit = (int) farmControlTerminal[3];
			}
		}
		if ("enable".equals(controlOperation)) {
			bits[enableBit] = 1;
		}
		controlTask.setControlTerminalbits(bits);
	}

	/**
	 * 阀门控制
	 * 
	 * @param controlTask
	 */
	public static void valveControl(ControlTask controlTask, List<Object[]> list) {
		String controlOperation = controlTask.getControlOperation();
		int[] bits = new int[32];
		int enableBit = 0;
		for (int i = 0; i < list.size(); i++) {
			Object[] farmControlTerminal = list.get(i);
			String functionCode = (String) farmControlTerminal[5];
			if ("enable".equals(functionCode)) {
				enableBit = (int) farmControlTerminal[3];
			}
		}
		if ("enable".equals(controlOperation)) {
			bits[enableBit] = 1;
		}
		controlTask.setControlTerminalbits(bits);

	}

	/**
	 * 水卷控制
	 * 
	 * @param controlTask
	 */
	public static void waterRollerShuttersControl(ControlTask controlTask, List<Object[]> list) {
		String controlOperation = controlTask.getControlOperation();
		int[] bits = new int[32];
		int enableBit = 0;
		for (int i = 0; i < list.size(); i++) {
			Object[] farmControlTerminal = list.get(i);
			String functionCode = (String) farmControlTerminal[5];
			if ("enable".equals(functionCode)) {
				enableBit = (int) farmControlTerminal[3];
			}
			if ("enable".equals(controlOperation)) {
				bits[enableBit] = 1;
			}
		}
		controlTask.setControlTerminalbits(bits);
	}

	/**
	 * 补光控制
	 * 
	 * @param controlTask
	 */
	public static void fillLightControl(ControlTask controlTask, List<Object[]> list) {
		String controlOperation = controlTask.getControlOperation();
		int[] bits = new int[32];
		int enableBit = 0;
		for (int i = 0; i < list.size(); i++) {
			Object[] farmControlTerminal = list.get(i);
			String functionCode = (String) farmControlTerminal[5];
			if ("enable".equals(functionCode)) {
				enableBit = (int) farmControlTerminal[3];
			}
			if ("enable".equals(controlOperation)) {
				bits[enableBit] = 1;
			}
		}
		controlTask.setControlTerminalbits(bits);
	
	}

	/**
	 * 风机控制
	 * 
	 * @param controlTask
	 */
	public static void draughtFanControl(ControlTask controlTask, List<Object[]> list) {
		String controlOperation = controlTask.getControlOperation();
		int[] bits = new int[32];
		int enableBit = 0;
		for (int i = 0; i < list.size(); i++) {
			Object[] farmControlTerminal = list.get(i);
			String functionCode = (String) farmControlTerminal[5];
			if ("enable".equals(functionCode)) {
				enableBit = (int) farmControlTerminal[3];
			}
			if ("enable".equals(controlOperation)) {
				bits[enableBit] = 1;
			}
		}
		controlTask.setControlTerminalbits(bits);
	
	}

	/**
	 * 温度控制
	 * 
	 * @param controlTask
	 */
	public static void temperatureControl(ControlTask controlTask, List<Object[]> list) {

	}

	/**
	 * 湿度控制
	 * 
	 * @param controlTask
	 */
	public static void humidityControl(ControlTask controlTask, List<Object[]> list) {

	}

	/**
	 * 二氧化碳控制
	 * 
	 * @param controlTask
	 */
	public static void carbonDioxideControl(ControlTask controlTask, List<Object[]> list) {
		String controlOperation = controlTask.getControlOperation();
		int[] bits = new int[32];
		int enableBit = 0;
		for (int i = 0; i < list.size(); i++) {
			Object[] farmControlTerminal = list.get(i);
			String functionCode = (String) farmControlTerminal[5];
			if ("enable".equals(functionCode)) {
				enableBit = (int) farmControlTerminal[3];
			}
			if ("enable".equals(controlOperation)) {
				bits[enableBit] = 1;
			}
		}
		controlTask.setControlTerminalbits(bits);
	
	}

	/**
	 * 氧气控制
	 * 
	 * @param controlTask
	 */
	public static void oxygenControl(ControlTask controlTask, List<Object[]> list) {

	}
}
