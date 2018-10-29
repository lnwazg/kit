package com.lnwazg.kit.converter.abst;

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
     * @ 
     */
    public abstract Object convertValue(String key);
    
    public String get(String key)
    {
        return convert2ValueConverter(key).get();
    }
    
    @Override
    public String getAsString(String key)
    {
        return getAs(key, String.class);
    }
    
    @Override
    public boolean getAsBoolean(String key)
    {
        return getAs(key, Boolean.class);
    }
    
    @Override
    public double getAsDouble(String key)
    {
        return getAs(key, Double.class);
    }
    
    @Override
    public float getAsFloat(String key)
    {
        return getAs(key, Float.class);
    }
    
    @Override
    public long getAsLong(String key)
    {
        return getAs(key, Long.class);
    }
    
    @Override
    public int getAsInt(String key)
    {
        return getAs(key, Integer.class);
    }
    
    @Override
    public byte getAsByte(String key)
    {
        return getAs(key, Byte.class);
    }
    
    @Override
    public short getAsShort(String key)
    {
        return getAs(key, Short.class);
    }
    
    @Override
    public <T> T getAs(String key, Class<T> clazz)
    {
        return convert2ValueConverter(key).getAs(clazz);
    }
    
    /**
    * 转换成一个值转换器
    * @author nan.li
    * @param key
    * @return
    */
    private ValueConverter convert2ValueConverter(String key)
    {
        return new ValueConverter(convertValue(key));
    }
}
