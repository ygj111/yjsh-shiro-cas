package com.hhh.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hhh.config.settings.ShiroSettings;
import com.hhh.shiro.DBRealm;

@Configuration
@EnableConfigurationProperties(ShiroSettings.class)
public class ShiroConfiguration {
	@Autowired
	private ShiroSettings shiroSettings;
	
	@Resource(name="shiroDBRealm")
	private Realm shiroDBRealm;
	
	@Resource(name="defaultWebSessionManager")
	private SessionManager defaultWebSessionManager;
	
	@Bean(name="securityManager")
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroDBRealm);
		securityManager.setSessionManager(defaultWebSessionManager);
		return securityManager;
	}
	
	/**
	 * 集群环境本地会话配置
	 * @return
	 */
	@Bean(name="defaultWebSessionManager")
	public SessionManager defaultWebSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(shiroSettings.getGlobalSessionTimeout());
		
		return sessionManager;
	}
	
	@Bean(name="shiroDBRealm")
	public AuthorizingRealm shiroDBRealm() {
		return new DBRealm();
	}
	
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
	
	@Bean(name="shiroFilter")
	public Filter shiroFilter() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());
		
		shiroFilterFactoryBean.setLoginUrl(shiroSettings.getLoginUrl());
		shiroFilterFactoryBean.setSuccessUrl(shiroSettings.getSuccessUrl());
		shiroFilterFactoryBean.setUnauthorizedUrl(shiroSettings.getUnauthorizedUrl());
		
		Map<String, Filter> filters = new HashMap<String, Filter>();
		filters.put("anon", new AnonymousFilter());
		filters.put("anthc", new FormAuthenticationFilter());
		shiroFilterFactoryBean.setFilters(filters);
		
		Map<String, String> filterChainDefMap = new LinkedHashMap<String, String>();
		filterChainDefMap.put("/login", "anthc");
		filterChainDefMap.put("/admin/**", "anthc");
		filterChainDefMap.put("/fund/**", "anthc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefMap);
		try {
			return (Filter)shiroFilterFactoryBean.getObject();
		} catch(Exception ex) {
			throw new BeanCreationException("shiroFilter", "FactoryBean throw exception on object creation", ex);
		}
	}
}
