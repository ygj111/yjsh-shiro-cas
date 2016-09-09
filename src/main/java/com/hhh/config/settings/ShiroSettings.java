package com.hhh.config.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="shiro")
public class ShiroSettings {
	private String loginUrl;
	private String successUrl;
	private String unauthorizedUrl;
	private long globalSessionTimeout;
	
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	public String getSuccessUrl() {
		return successUrl;
	}
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}
	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
	public long getGlobalSessionTimeout() {
		return globalSessionTimeout;
	}
	public void setGlobalSessionTimeout(long globalSessionTimeout) {
		this.globalSessionTimeout = globalSessionTimeout;
	}
	
	
}
