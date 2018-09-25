package com.lnwazg.kit.cache;

import java.util.concurrent.TimeUnit;

/**
 * 简易版的缓存工具，数据全部存储在内存中，存取速度很快，但是写入太多数据就会有OOM的问题<br>
 * 适合存储少量需要快速存取的数据<br>
 * 可指定缓存的失效时间<br>
 * 如果想安全地存入任意大小的数据，可使用FileCacheLite，无OOM问题，但是存取速度稍慢
 * @author nan.li
 * @version 2016年4月14日
 */
public class JvmMemCacheLite
{
    /**
     * lite，一个唯一的共享实例
     */
    static JvmMemCache<Object> lite = new JvmMemCache<>();
    
    /**
     * 从JVM缓存中获取数据，给定指定的失效时间参数
     * 
     * @author nan.li
     * @param key
     * @param queryAllGoodsCacheMinutes
     * @param minutes
     * @return
     */
    public static Object get(String key, int failTime, TimeUnit timeUnit)
    {
        return lite.get(key, failTime, timeUnit);
    }
    
    public static Object get(String key)
    {
        return lite.get(key);
    }
    
    /**
     * 存入一个对象到JVM的缓存中
     * 
     * @author nan.li
     * @param key
     * @param obj
     */
    public static void put(String key, Object obj)
    {
        lite.put(key, obj);
    }
    
    /**
     * 检查缓存对象中是否有某个键
     * 
     * @author nan.li
     * @param key
     * @return
     */
    public static boolean containsKey(String key)
    {
        return lite.containsKey(key);
    }
    
    /**
     * 检查缓存对象中是否有某个键<br>
     * 指定了缓存失效时间
     * @author nan.li
     * @param key
     * @return
     */
    public boolean containsKey(String key, int failTime, TimeUnit timeUnit)
    {
        return lite.containsKey(key, failTime, timeUnit);
    }
}
