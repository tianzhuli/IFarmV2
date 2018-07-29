package com.ifarm.redis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserRedisUtil {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void setUserToken(String userId, String token) {
		redisTemplate.opsForValue().set(RedisContstant.USER_TOKEN + userId, token);
		redisTemplate.expire(RedisContstant.USER_TOKEN + userId, RedisContstant.USER_TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
	}

	public String getUserToken(String userId) {
		return redisTemplate.opsForValue().get(RedisContstant.USER_TOKEN + userId);
	}

	public void delUserToken(String userId) {
		redisTemplate.delete(RedisContstant.USER_TOKEN + userId);
	}

	public void saveUserDetail(String userId, HashMap<String, String> map) {
		HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
		hashOperations.putAll(RedisContstant.USER_DETAIL + userId, map);
	}

	public void updateUserDetail(String userId, String key, String value) {
		HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
		hashOperations.put(RedisContstant.USER_DETAIL + userId, key, value);
	}

	public void delUserDetail(String userId) {
		redisTemplate.delete(RedisContstant.USER_DETAIL + userId);
	}

	public void setUserSignature(String userId, String token) {
		redisTemplate.opsForValue().set(RedisContstant.USER_SIGNATURE + userId, token);
		redisTemplate.expire(RedisContstant.USER_SIGNATURE + userId, RedisContstant.USER_SIGNATURE_EXPIRE_TIME, TimeUnit.DAYS);
	}

	public String getUserSignature(String userId) {
		return redisTemplate.opsForValue().get(RedisContstant.USER_SIGNATURE + userId);
	}

	public void delUserSignature(String userId) {
		redisTemplate.delete(RedisContstant.USER_SIGNATURE + userId);
	}

	public void setUserControlResultMessageCache(String userId, String message) {
		redisTemplate.opsForList().rightPush(RedisContstant.USER_CONTROL_RESULT_MESSAGE_CACHE + userId, message);
	}

	public List<String> getUserControlResultMessageCache(String userId) {
		String redisKey = RedisContstant.USER_CONTROL_RESULT_MESSAGE_CACHE + userId;
		List<String> list = new ArrayList<String>();
		ListOperations<String, String> listOperations = redisTemplate.opsForList();
		while (listOperations.size(redisKey) > 0) {
			list.add(listOperations.leftPop(redisKey));
		}
		return list;
	}

	public void delUserControlResultMessageCache(String userId) {
		redisTemplate.delete(RedisContstant.USER_CONTROL_RESULT_MESSAGE_CACHE + userId);
	}

}
