package com.ifarm.redis.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FarmRedisUtil {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void saveFarmDetail(Integer farmId, HashMap<String, String> hashMap) {
		HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
		hashOperations.putAll(RedisContstant.FARM_DETAIL + farmId, hashMap);
	}

	public void updateFarmDetail(Integer farmId, String hashKey, String value) {
		HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
		hashOperations.put(RedisContstant.FARM_DETAIL + farmId, hashKey, value);
	}

	public String getFarmDetailByKey(Integer farmId, String hashKey) {
		return (String) redisTemplate.opsForHash().get(RedisContstant.FARM_DETAIL + farmId, hashKey);
	}

	public Map<String, String> getFarmDetailById(Integer farmId) {
		HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
		return hashOperations.entries(RedisContstant.FARM_DETAIL + farmId);
	}
	
	public void delFarmDetail(Integer farmId) {
		redisTemplate.delete(RedisContstant.FARM_DETAIL + farmId);
	}
}
