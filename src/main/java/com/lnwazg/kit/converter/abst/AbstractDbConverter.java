package com.lnwazg.kit.converter.abst;

import java.sql.SQLException;

import com.lnwazg.kit.converter.ValueConverter;
import com.lnwazg.kit.converter.iface.DbConvertAware;

/**
 * 抽象的转换器，是一个转换器模板<br>
 * 只需要实现转换算法：convertValue(key)，那么该方法会自动帮你实现键值转换功能，并且很便捷地得到想要的值类型
 * @author nan.li
 * @version 2017年7月23日
 */
public abstract class AbstractDbConverter implements DbConvertAware
{
    /**
     * 键值转换算法，待子类去实现
     * @author nan.li
     * @param key
     * @return
     * @throws SQLException 
     */
    public abstract Object convertValue(String key)
        throws SQLException;
        
    /**
    * 转换成一个值转换器
    * @author nan.li
    * @param key
    * @return
     * @throws SQLException 
    */
    public ValueConverter convert2ValueConverter(String key)
        throws SQLException
    {
        return new ValueConverter(convertValue(key));
    }
    
    @Override
    public String getAsString(String key)
        throws SQLException
    {
        return convert2ValueConverter(key).getAsString();
    }
    
    @Override
    public boolean getAsBoolean(String key)
        throws SQLException
    {
        return convert2ValueConverter(key).getAsBoolean();
    }
    
    @Override
    public double getAsDouble(String key)
        throws SQLException
    {
        return convert2ValueConverter(key).getAsDouble();
    }
    
    @Override
    public float getAsFloat(String key)
        throws SQLException
    {
        return convert2ValueConverter(key).getAsFloat();
    }
    
    @Override
    public long getAsLong(String key)
        throws SQLException
    {
        return convert2ValueConverter(key).getAsLong();
    }
    
    @Override
    public int getAsInt(String key)
        throws SQLException
    {
        return convert2ValueConverter(key).getAsInt();
    }
    
    @Override
    public byte getAsByte(String key)
        throws SQLException
    {
        return convert2ValueConverter(key).getAsByte();
    }
    
    @Override
    public short getAsShort(String key)
        throws SQLException
    {
        return convert2ValueConverter(key).getAsShort();
    }
    
    @Override
    public <T> T getAs(String key, Class<T> clazz)
        throws SQLException
    {
        return convert2ValueConverter(key).getAs(clazz);
    }
}
