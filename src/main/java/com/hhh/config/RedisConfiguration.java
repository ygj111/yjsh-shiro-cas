package com.hhh.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.hhh.config.settings.RedisSettings;
import com.hhh.redis.dao.RedisDao;
import com.hhh.shiro.RedisSessionDao;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableConfigurationProperties(RedisSettings.class)
public class RedisConfiguration {
	@Autowired
	private RedisSettings redisSettings;
	
	@Resource(name="jedisPoolConfig")
	private JedisPoolConfig jedisPoolConfig;
	
	@Resource(name="jedisConnectionFactory")
	private JedisConnectionFactory jedisConnectionFactory;
	
	@Resource(name="redisTemplate")
	private RedisTemplate<byte[], byte[]> redisTemplate;
	
	@Resource(name="redisDao")
	private RedisDao redisDao;
	
	@Bean(name="jedisPoolConfig")
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(redisSettings.getMaxIdle());
		poolConfig.setMaxWaitMillis(redisSettings.getMaxWait());
		poolConfig.setTestOnBorrow(Boolean.parseBoolean(redisSettings.getTestOnBorrow()));
		
		return poolConfig;
	}
	
	@Bean(name="jedisConnectionFactory")
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory connFactory = new JedisConnectionFactory();
		connFactory.setHostName(redisSettings.getHost());
		connFactory.setPort(redisSettings.getPort());
		connFactory.setPassword(redisSettings.getPass());
		connFactory.setPoolConfig(jedisPoolConfig);
//		connFactory.setDatabase(1);
		
		return connFactory;
	}
	
	@Bean(name="redisTemplate")
	public RedisTemplate<byte[], byte[]> redisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<byte[], byte[]>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		
		return redisTemplate;
	}
	
	@Bean
	public RedisSessionDao redisSessionDao() {
		RedisSessionDao redisSessionDao = new RedisSessionDao();
		redisSessionDao.setRedisDao(redisDao);
		
		return redisSessionDao;
	}
	
	@Bean(name="redisDao")
	public RedisDao redisDao() {
		RedisDao redisDao = new RedisDao();
		redisDao.setRedisTemplate(redisTemplate);
		
		return redisDao;
	}
}
