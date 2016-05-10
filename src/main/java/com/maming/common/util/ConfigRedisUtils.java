package com.maming.common.util;

import java.util.Map;

import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ConfigRedisUtils {
	
	public static Logger errorLog = LogWriter.getErrorLog();
	
    private static ConfigRedisUtils instance = new ConfigRedisUtils();
    
    PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();
    
    private JedisPool jedisPool;

    private ConfigRedisUtils(){
    	try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static ConfigRedisUtils getInstance() {
        return instance;
    }

    public void init() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(propertiesUtils.getRedisMaxTotal());
        config.setMaxIdle(10);
        config.setTestOnBorrow(true);
        config.setTestWhileIdle(true);
        config.setBlockWhenExhausted(false);
        config.setMaxWaitMillis(3000);
        jedisPool = new JedisPool(config, propertiesUtils.getRedisHost(), Integer.parseInt(propertiesUtils.getRedisPort()));
        // 测试连接池是否可用
        Jedis jedis = jedisPool.getResource();
        jedisPool.returnResource(jedis);
    }

    public String getValue(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
        	errorLog.error(e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    public void setValue(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            errorLog.error(e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Map<String, String> getAllHashValue(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            errorLog.error(e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }
}
