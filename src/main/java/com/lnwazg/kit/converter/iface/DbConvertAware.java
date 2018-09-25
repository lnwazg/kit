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
     * 以String的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    String getAsString(String key)
        throws SQLException;
        
    /**
     * 以boolean的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    boolean getAsBoolean(String key)
        throws SQLException;
        
    /**
     * 以double的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    double getAsDouble(String key)
        throws SQLException;
        
    /**
     * 以float的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    float getAsFloat(String key)
        throws SQLException;
        
    /**
     * 以long的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    long getAsLong(String key)
        throws SQLException;
        
    /**
     * 以int的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    int getAsInt(String key)
        throws SQLException;
        
    /**
     * 以byte的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    byte getAsByte(String key)
        throws SQLException;
        
    /**
     * 以short的方式获取某个key
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    short getAsShort(String key)
        throws SQLException;
}
