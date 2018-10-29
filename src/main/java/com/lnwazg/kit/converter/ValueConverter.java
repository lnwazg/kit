package com.lnwazg.kit.converter;

import java.lang.reflect.Type;

import org.apache.commons.lang.ObjectUtils;

import com.lnwazg.kit.gson.GsonKit;

/**
 * 对象值转换器<br>
 * 将某个对象以指定的类型返回，例如getAsString()、getAsBoolean()等等
 * @author nan.li
 * @version 2017年7月22日
 */
public class ValueConverter
{
    /**
     * 数值对象
     */
    private Object value;
    
    /**
     * 便捷的使用入口
     * @author nan.li
     * @param value
     * @return
     */
    public static ValueConverter of(Object value)
    {
        return new ValueConverter(value);
    }
    
    /**
     * 构造函数 
     * @param value
     */
    public ValueConverter(Object value)
    {
        this.value = value;
    }
    
    /**
     * 默认的获取值的方式
     * @author nan.li
     * @return
     */
    public String get()
    {
        return ObjectUtils.toString(value);
    }
    
    public String value()
    {
        return get();
    }
    
    public String getAsString()
    {
        return get();
    }
    
    public String stringValue()
    {
        return getAsString();
    }
    
    public boolean getAsBoolean()
    {
        return Boolean.valueOf(get());
    }
    
    public boolean booleanValue()
    {
        return getAsBoolean();
    }
    
    public double getAsDouble()
    {
        return Double.valueOf(get());
    }
    
    public double doubleValue()
    {
        return getAsDouble();
    }
    
    public float getAsFloat()
    {
        return Float.valueOf(get());
    }
    
    public long getAsLong()
    {
        return Long.valueOf(get());
    }
    
    public long longValue()
    {
        return getAsLong();
    }
    
    public int getAsInt()
    {
        return Integer.valueOf(get());
    }
    
    public int intValue()
    {
        return getAsInt();
    }
    
    public byte getAsByte()
    {
        return Byte.valueOf(get());
    }
    
    public byte byteValue()
    {
        return getAsByte();
    }
    
    public short getAsShort()
    {
        return Short.valueOf(get());
    }
    
    public short shortValue()
    {
        return getAsShort();
    }
    
    public <T> T getAs(Class<T> clazz)
    {
        String value = get();
        return GsonKit.parseString2Object(value, clazz);
    }
    
    public <T> T getAsClass(Class<T> clazz)
    {
        return getAs(clazz);
    }
    
    public <T> T classValue(Class<T> clazz)
    {
        return getAs(clazz);
    }
    
    public <T> T getAs(Type type)
    {
        String value = get();
        return GsonKit.parseString2Object(value, type);
    }
    
    public <T> T getAsType(Type type)
    {
        return getAs(type);
    }
    
    public <T> T typeValue(Type type)
    {
        return getAs(type);
    }
}
