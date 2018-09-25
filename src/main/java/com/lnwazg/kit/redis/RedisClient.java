package com.lnwazg.kit.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis客户端工具
 * @author nan.li
 * @version 2018年9月25日
 */
public class RedisClient
{
    private Jedis jedis;
    
    private JedisPool jedisPool;
    
    public RedisClient()
    {
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        jedisPool = new JedisPool(config, "127.0.0.1", 6379);
        jedis = jedisPool.getResource();
    }
    
    /**
     * redis的一般般的测试
     * @author nan.li
     */
    public void test()
    {
        jedis.set("aaa", "111");
        System.out.println(jedis.get("aaa"));
        jedis.close();
    }
    
    public static void main(String[] args)
    {
        new RedisClient().test();
    }
    
}