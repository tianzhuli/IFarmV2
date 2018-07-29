package com.ifarm.redis.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FarmCollectorRedisHelper {

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, Long> valueRedisTemplate;

	public void setFarmCollectorCache(Long collectorId, Long deviceId) {
		valueRedisTemplate.opsForList().rightPush(RedisContstant.FARM_COLLECTOR_CACHE + collectorId.toString(), deviceId);
	}

	public List<Long> getFarmCollectorCache(Long collectorId) {
		List<Long> list = new ArrayList<Long>();
		String redisKey = RedisContstant.FARM_COLLECTOR_CACHE + collectorId.toString();
		ListOperations<String, Long> listOperations = valueRedisTemplate.opsForList();
		while (listOperations.size(redisKey) > 0) {
			list.add(listOperations.leftPop(redisKey));
		}
		return list;
	}
}
