package com.ifarm.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.ifarm.bean.CollectorCommand;
import com.ifarm.bean.DeviceValueBase;

@Component
public class CollectorDeviceCommandRedisHelper extends BaseLockRedisHelper<String, CollectorCommand>{
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, DeviceValueBase> valueRedisTemplate;
	
	public CollectorDeviceCommandRedisHelper() {
		setRedisKeyName(RedisContstant.COLLECTOR_COMMAND);
	}
}
