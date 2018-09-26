package com.lnwazg.kit.cache;

import com.lnwazg.kit.gson.GsonKit;

/**
 * 缓存客户端的通用接口
 * @author nan.li
 * @version 2018年9月26日
 */
public interface CacheClient
{
    public void put(String key, String value);
    
    public void put(String key, String value, int expireSeconds);
    
    public String get(String key);
    
    /**
     * 将任意对象存入缓存
     * @author nan.li
     * @param key
     * @param t
     */
    default public <T> void putObj(String key, T t)
    {
        String tStr = GsonKit.parseObject2String(t);
        put(key, tStr);
    }
    
    /**
     * 将任意对象存入缓存
     * @author nan.li
     * @param key
     * @param t
     * @param expireSeconds
     */
    default public <T> void putObj(String key, T t, int expireSeconds)
    {
        String tStr = GsonKit.parseObject2String(t);
        put(key, tStr, expireSeconds);
    }
    
    /**
     * 根据key获取对应的值，并转换为对象
     * @author nan.li
     * @param key
     * @param clazz
     * @return
     */
    default public <T> T getObj(String key, Class<T> clazz)
    {
        String tStr = get(key);
        return GsonKit.parseString2Object(tStr, clazz);
    }
    
}
