package com.hhh.redis.dao;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisDao {
	private RedisTemplate<byte[], byte[]> redisTemplate;
	
	/**
	 * 写入数据
	 * @param key
	 * @param value
	 */
	public void set(byte[] key, byte[] value) {
		redisTemplate.opsForValue().set(key, value);
	}
	
	/**
	 * 写入数据，并保存expireTime时长
	 * @param key
	 * @param value
	 * @param expiredTime
	 */
	public void set(byte[] key, byte[] value, int expiredTime) {
		redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.SECONDS);
	}
	
	/**
	 * 获取数据
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	/**
	 * 删除
	 * @param key
	 */
	public void del(byte[] key) {
		redisTemplate.delete(key);
	}
	
	/**
	 * 获取所有的Key
	 * @return
	 */
	public Set<byte[]> keys(String pattern) {
		return redisTemplate.keys(pattern.getBytes());
	}

	public RedisTemplate<byte[], byte[]> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<byte[], byte[]> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	
}
