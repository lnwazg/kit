package com.lnwazg.kit.redis;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.lnwazg.kit.gson.GsonKit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis客户端工具<br>
 * 支持字符串存取、对象存取
 * @author nan.li
 * @version 2018年9月25日
 */
public class RedisClient
{
    // 连接 redis 等待时间
    private static final int CONNECT_TIMEOUT = 10000;
    
    // 可用连接实例的最大数目，默认值为8；
    // 如果赋值为-1，则表示不限制；
    // 如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
    private static final int MAX_TOTAL = 1024;
    
    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值为8
    private static final int MAX_IDLE = 200;
    
    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。
    // 如果超过等待时间，则直接抛出JedisConnectionException
    private static final int MAX_WAIT_MILLS = 10000;
    
    // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
    private static final boolean IS_TEST_ON_BORROW = true;
    
    // 连接池实例
    private JedisPool jedisPool = null;
    
    public RedisClient(String address, int port)
    {
        this(address, port, null);
    }
    
    /**
     * 构造函数 
     * @param address 服务器IP
     * @param port    端口号
     * @param password  密码
     */
    public RedisClient(String address, int port, String password)
    {
        try
        {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT_MILLS);
            config.setTestOnBorrow(IS_TEST_ON_BORROW);
            if (StringUtils.isEmpty(password))
            {
                //若密码为空，则不校验密码
                jedisPool = new JedisPool(config, address, port, CONNECT_TIMEOUT);
            }
            else
            {
                //否则，校验密码
                jedisPool = new JedisPool(config, address, port, CONNECT_TIMEOUT, password);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 从连接池中获取 Jedis 实例
     * @author nan.li
     * @return
     */
    private Jedis getJedis()
    {
        if (jedisPool != null)
        {
            return jedisPool.getResource();
        }
        return null;
    }
    
    /**
     * 将一个键值对存入redis
     * @author nan.li
     * @param key
     * @param value
     */
    public void put(String key, String value)
    {
        Jedis jedis = getJedis();
        jedis.set(key, value);
        jedis.close();
    }
    
    /**
     * 将任意对象存入redis
     * @author nan.li
     * @param key
     * @param t
     */
    public <T> void putObj(String key, T t)
    {
        String tStr = GsonKit.parseObject2String(t);
        put(key, tStr);
    }
    
    /**
     * 将一个键值对存入redis，并设置过期时间（单位：秒）
     * @author nan.li
     * @param key
     * @param value
     * @param expireSeconds
     */
    public void put(String key, String value, int expireSeconds)
    {
        Jedis jedis = getJedis();
        jedis.setex(key, expireSeconds, value);
        jedis.close();
    }
    
    /**
     * 将任意对象存入redis
     * @author nan.li
     * @param key
     * @param t
     * @param expireSeconds
     */
    public <T> void putObj(String key, T t, int expireSeconds)
    {
        String tStr = GsonKit.parseObject2String(t);
        put(key, tStr, expireSeconds);
    }
    
    /**
     * 根据key获取对应的值
     * @author nan.li
     * @param key
     * @return
     */
    public String get(String key)
    {
        Jedis jedis = getJedis();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }
    
    /**
     * 根据key获取对应的值，并转换为对象
     * @author nan.li
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getObj(String key, Class<T> clazz)
    {
        String tStr = get(key);
        return GsonKit.parseString2Object(tStr, clazz);
    }
    
    public static void main(String[] args)
    {
        RedisClient redisClient = new RedisClient("127.0.0.1", 6379, null);
        System.out.println(redisClient.get("aaa"));
        System.out.println(redisClient.get("bbb"));
        
        redisClient.put("aaa", "1111", 5);
        System.out.println(redisClient.get("aaa"));
        
        redisClient.putObj("bbb", new Date(), 5);
        System.out.println(redisClient.get("bbb"));
    }
}