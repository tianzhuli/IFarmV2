package com.ifarm.redis.util;

import org.springframework.stereotype.Component;

import com.ifarm.bean.CollectorValue;

@Component
public class FarmCollectorValueRedisUtil extends BaseRedisHelper<Long, CollectorValue>{
	
	public FarmCollectorValueRedisUtil() {
		setRedisKeyName(RedisContstant.FARM_COLLECTOR_STATE);
	}
}
