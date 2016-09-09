package com.hhh.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsonorg.XSON;
import com.hhh.redis.dao.RedisDao;

/**
 * 用Redis替换本地内存来存储Session
 * @author mars.zhong
 *
 */
public class RedisSessionDao extends AbstractSessionDAO {
	private static Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);
	
	private RedisDao redisDao;
	
	private String keyPrefix = "session";
	
	/**
	 * 创建Session
	 */
	@Override 
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        storeSession(session);
        
		return sessionId;
	 }
	
	/**
	 * 读取Session
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			logger.error("Session id不能为空！");
			throw new NullPointerException("Session id cannot be null!");
		}
		
		byte[] key = getByteKey(sessionId);
		Session session = XSON.parse(redisDao.get(key));
		
		return session;
	}

	/**
	 * 更新Session
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		storeSession(session);
	}
	
	/**
	 * 删除Session
	 */
	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			logger.error("Sesion或者Session id不能为null！");
			throw new NullPointerException("Session or Session id cannot be null!");
		}
		redisDao.del(getByteKey(session.getId()));
	}
	
	/**
	 * 获取所有活动的session
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<Session>();
		
		// 查询所有SessionKey
		Set<byte[]> keys = redisDao.keys(keyPrefix + "*");
		
		if (keys != null && keys.size() > 0) {
			for (byte[] key: keys) {
				Session session =  XSON.parse(redisDao.get(key));
				sessions.add(session);
			}
		}
		return sessions;
	}
	
	/**
	 * 存储Session
	 * @param session
	 * @throws UnknownSessionException
	 */
	private void storeSession(Session session) 
			throws UnknownSessionException{
		if (session == null || session.getId() == null) {
			logger.error("Sesion或者Session id不能为null！");
			throw new NullPointerException("Session or Session id cannot be null!");
		}
		
		byte[] key = getByteKey(session.getId());
		byte[] value = XSON.write(session);
		redisDao.set(key, value);
	}
	
	/**
	 * 获取构造后的sessionId
	 * @param sessionId
	 * @return
	 */
	private byte[] getByteKey(Serializable sessionId) {
		String byteKey = keyPrefix + "_" + sessionId;
		return byteKey.getBytes();
	}

	public RedisDao getRedisDao() {
		return redisDao;
	}

	public void setRedisDao(RedisDao redisDao) {
		this.redisDao = redisDao;
	}
	
	
}
