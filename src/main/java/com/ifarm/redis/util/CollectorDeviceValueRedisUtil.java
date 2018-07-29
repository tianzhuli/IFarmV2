package com.ifarm.redis.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.ifarm.bean.DeviceValueBase;
import com.ifarm.util.CacheDataBase;

@Component
public class CollectorDeviceValueRedisUtil {
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, DeviceValueBase> valueRedisTemplate;

	public void saveCollectorDeviceValue(Integer farmId, Long collectorDeviceId, DeviceValueBase deviceValueBase) {
		HashOperations<String, Long, DeviceValueBase> hashOperations = valueRedisTemplate.opsForHash();
		hashOperations.put(RedisContstant.FARM_COLLECTOR_DEVICE_MAIN_VALUE + farmId.toString(), collectorDeviceId, deviceValueBase);
	}

	public DeviceValueBase getCollectorDeviceValue(Integer farmId, Long deviceId) {
		HashOperations<String, Long, DeviceValueBase> hashOperations = valueRedisTemplate.opsForHash();
		return hashOperations.get(RedisContstant.FARM_COLLECTOR_DEVICE_MAIN_VALUE + farmId.toString(), deviceId);
	}

	public List<DeviceValueBase> getCollectorDeviceCacheValue(Long deviceId) {
		String redisKeyString = RedisContstant.FARM_COLLECTOR_DEVICE_MAIN_CACHE_VALUE + deviceId.toString();
		ListOperations<String, DeviceValueBase> listOperations = valueRedisTemplate.opsForList();
		return listOperations.range(redisKeyString, 0, CacheDataBase.cacheSize);
	}

	public void saveCollectorDeviceCacheValue(Long deviceId, DeviceValueBase deviceValueBase) {
		String redisKeyString = RedisContstant.FARM_COLLECTOR_DEVICE_MAIN_CACHE_VALUE + deviceId.toString();
		ListOperations<String, DeviceValueBase> listOperations = valueRedisTemplate.opsForList();
		if (listOperations.size(redisKeyString) >= CacheDataBase.cacheSize) {
			listOperations.rightPop(redisKeyString);
		}
		listOperations.leftPush(redisKeyString, deviceValueBase);
	}

}
