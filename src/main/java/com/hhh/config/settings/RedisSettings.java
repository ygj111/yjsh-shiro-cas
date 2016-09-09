package com.hhh.config.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="redis")
public class RedisSettings {
	private String host;
	private int port;
	private String pass;
	private int maxIdle;
	private int maxActive;
	private int maxWait;
	private String testOnBorrow;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public int getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	public int getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}
	public String getTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(String testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	
	
}
