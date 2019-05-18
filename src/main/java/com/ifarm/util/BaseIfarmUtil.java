package com.ifarm.util;

import com.alibaba.fastjson.JSONObject;
import com.ifarm.constant.IfarmConfigConstant;
import com.ifarm.constant.SystemConfigCache;
import com.ifarm.enums.ControlStrategyEnum;

public class BaseIfarmUtil {

	public static boolean isSwithMultiControl() {
		return (boolean) CacheDataBase.systemConfigCacheMap
				.get(SystemConfigCache.MULTIPLE_CONTROL_SYSTEM);
	}

	public static boolean isPreBoot(String controlType) {
		String controlStrategy = ((JSONObject) CacheDataBase.farmConfigCacheMap
				.get(IfarmConfigConstant.CONTROL_SYSTEM_STRATEGY))
				.getString(controlType);
		return StringUtil.equals(controlStrategy,
				ControlStrategyEnum.PRE_BOOT_STRATEGY.getCode());
	}
	
	public static boolean isEnableMultiFunction(String controlType) {
		String controlStrategy = ((JSONObject) CacheDataBase.farmConfigCacheMap
				.get(IfarmConfigConstant.CONTROL_SYSTEM_STRATEGY))
				.getString(controlType);
		return StringUtil.equals(controlStrategy,
				ControlStrategyEnum.MULTI_FUNCTION_STRATEGY.getCode());
	}
}
