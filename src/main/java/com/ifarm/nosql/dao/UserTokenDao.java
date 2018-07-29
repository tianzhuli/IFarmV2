package com.ifarm.nosql.dao;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.ifarm.nosql.bean.UserToken;

@Repository
public class UserTokenDao {
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RedisTemplate<String, String> template;

	@Autowired
	private RedisTemplate<String, byte[]> byteTemplate;
	
	/**
	 * RedisTemplate 的key和value需要序列化，redis默认有
	 * RedisSerializer,JdkSerializationRedisSerializer
	 * 还有Jackson2JsonRedisSerializer
	 * ，JacksonJsonRedisSerializer，GenericToStringSerializer
	 * ，StringRedisSerializer，OxmSerializer 在context配置中以及配置了serialize，否则需要
	 * template.setKeySerializer(serializer)
	 * 
	 * @param userToken
	 */
	public void saveUserToken(UserToken userToken) {
		redisTemplate.opsForValue().set(userToken.getUserId() + "_" + "token", userToken.getTokenId(), 30, TimeUnit.MINUTES);
		BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(userToken.getUserId() + "_" + "time");
		ops.put("token", userToken.getTokenId());
		ops.put("time", userToken.getTokenTime());
		template.opsForValue().set("token_test", userToken.getTokenId());
		template.delete(userToken.getUserId() + "_list");
		BoundListOperations<String, String> operations = template.boundListOps(userToken.getUserId() + "_list");
		operations.leftPush(userToken.getTokenId());
		operations.leftPush(userToken.getUserId());
		operations.rightPush(userToken.getTokenTime());
		operations.expire(30, TimeUnit.MINUTES);
		
	}

	public String getUserToken(String userId) {
		String token = redisTemplate.opsForValue().get(userId + "_token");
		return token;
	}

}
