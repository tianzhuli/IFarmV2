package com.ifarm.redis.util;

public class RedisContstant {
	
	public static final String USER_TOKEN = "user_token_";
	public static final String USER_SIGNATURE = "user_signature_";
	public static final String USER_CONTROL_RESULT_MESSAGE_CACHE = "user_control_result_message_cache_";
	
	public static final long USER_TOKEN_EXPIRE_TIME = 10;// minutes
	public static final long USER_SIGNATURE_EXPIRE_TIME = 15;// day
	
	public static final String USER_DETAIL = "user_detail_";

	public static final String FARM_DETAIL = "farm_detail_";
	
	public static final String FARM_COLLECTOR_CACHE = "farm_collector_cache_";
	
	public static final String FARM_COLLECTOR_STATE = "farm_collector_state_";
		
	public static final String FARM_COLLECTOR_DEVICE_MAIN_CACHE_VALUE = "farm_collector_device_main_cache_value_";
	
	public static final String FARM_COLLECTOR_DEVICE_MAIN_VALUE = "farm_collector_device_main_value_";
	
	public static final String PRODUCT_DEVICE_TYPE = "production_device_type_";

	public static final String PRODUCT_DEVICE_CATEGORY = "production_device_category";
	
	public static final String FARM_CONTROL_SYSTEM_TYPE = "farm_control_system_type";
	
	public static final String FARM_CONTROL_TERMINAL = "farm_control_terminal_";
	
	public static final String MANAGER_TOKEN = "manager_token_";
	
	public static final String CONTROL_COMMAND_CACHE = "control_command_cache_";
	
	public static final String CONTROL_TASK_CACHE = "control_task_cache_";
	public static final String WFM_CONTROL_TASK_CACHE = "wfm_control_task_cache_";

}
