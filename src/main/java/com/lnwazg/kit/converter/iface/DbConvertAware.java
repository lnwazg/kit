package com.lnwazg.kit.converter.iface;

import java.sql.SQLException;

/**
 * 通用的配置必须要实现的接口
 * @author nan.li
 * @version 2017年7月22日
 */
public interface DbConvertAware
{
    /**
     * 获取这个key的原始值
     * @author nan.li
     * @param key
     * @return
     */
    String get(String key);
    
    /**
     * 以String的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    String getAsString(String key);
    
    /**
     * 以boolean的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    boolean getAsBoolean(String key);
    
    /**
     * 以double的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    double getAsDouble(String key);
    
    /**
     * 以float的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    float getAsFloat(String key);
    
    /**
     * 以long的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    long getAsLong(String key);
    
    /**
     * 以int的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    int getAsInt(String key);
    
    /**
     * 以byte的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    byte getAsByte(String key);
    
    /**
     * 以short的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    short getAsShort(String key);
    
    /**
     * 以某种Class的方式获取某个key
     * @author nan.li
     * @param key
     * @param clazz
     * @return
     */
    <T> T getAs(String key, Class<T> clazz);
}
