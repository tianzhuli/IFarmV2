package com.ifarm.redis.util;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductionDeviceUtil {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	public void saveProductionDeviceType(HashMap<String, String> hashMap) {
		HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
		hashOperations.putAll(RedisContstant.PRODUCT_DEVICE_TYPE, hashMap);
	}

	public void saveProductionDeviceCategory(HashMap<String, String> hashMap) {
		HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
		hashOperations.putAll(RedisContstant.PRODUCT_DEVICE_CATEGORY, hashMap);
	}

}
