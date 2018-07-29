package com.ifarm.redis.util;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class BaseRedisHelper<K, V> {

	protected String redisKeyName;

	@Autowired
	@Qualifier("redisTemplate")
	protected RedisTemplate<String, V> valueRedisTemplate;

	public String getRedisKeyName() {
		return redisKeyName;
	}

	public void setRedisKeyName(String redisKeyName) {
		this.redisKeyName = redisKeyName;
	}

	public V getRedisMapValue(String key, K hashKey) {
		HashOperations<String, K, V> hashOperations = valueRedisTemplate.opsForHash();
		return hashOperations.get(redisKeyName + key, hashKey);
	}

	public void setRedisMapValue(String key, K hashKey, V value) {
		HashOperations<String, K, V> hashOperations = valueRedisTemplate.opsForHash();
		hashOperations.put(redisKeyName + key, hashKey, value);
	}

	public void deleteRedisMapValue(String key, K hashKeys) {
		HashOperations<String, K, V> hashOperations = valueRedisTemplate.opsForHash();
		hashOperations.delete(redisKeyName + key, hashKeys);
	}

	public void setRedisStringValue(String key, V value) {
		ValueOperations<String, V> valueOperations = valueRedisTemplate.opsForValue();
		valueOperations.set(redisKeyName + key, value);
	}

	public V getRedisStringValue(String key) {
		ValueOperations<String, V> valueOperations = valueRedisTemplate.opsForValue();
		return valueOperations.get(redisKeyName + key);
	}

	public void deleteRedisStringKey(String key) {
		valueRedisTemplate.delete(redisKeyName + key);
	}

	public void addRedisListValue(String key, V value) {
		valueRedisTemplate.opsForList().rightPush(redisKeyName + key, value);
	}

	public void replaceRedisListValues(String key, List<V> values) {
		valueRedisTemplate.delete(redisKeyName + key);
		if (values != null && values.size() != 0) {
			valueRedisTemplate.opsForList().rightPushAll(redisKeyName + key, values);
		}
	}

	public V getRedisListValue(String key) {
		ListOperations<String, V> listOperations = valueRedisTemplate.opsForList();
		return listOperations.leftPop(redisKeyName + key);
	}

	public List<V> getRedisListValues(String key) {
		String redisKey = redisKeyName + key;
		ListOperations<String, V> listOperations = valueRedisTemplate.opsForList();
		List<V> list = listOperations.range(redisKey, 0, listOperations.size(redisKey));
		return list;
	}

	public void setRedisListIndexValue(String key, V value, long index) {
		valueRedisTemplate.opsForList().set(redisKeyName + key, index, value);
	}

	public Set<String> keysSet() {
		return valueRedisTemplate.keys(redisKeyName + "*");
	}
}
