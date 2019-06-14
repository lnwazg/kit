package com.lnwazg.kit.security;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 敏感信息脱敏输出工具类
 * @author li.nan
 */
public class SensitiveInfoKit
{
    private static final Log logger = LogFactory.getLog(SensitiveInfoKit.class);
    
    /**
     * 敏感字段列表
     */
    static String[] sensitiveFields = {"pan", "signId", "idCode", "reqMsg"};
    
    /**
     * 脱敏替换后的内容
     */
    static String replacement = "***";
    
    /**
     * 采用默认的敏感字段列表，对于非空的敏感信息进行脱敏，然后toString<br>
     * 方法后续扩展：敏感字段若有变动则直接在这个方法里统一增加即可<br>
     * 不会改变原有对象的实际属性值
     * @param obj
     * @return
     */
    public static String reflectionToString(Object obj)
    {
        //对象拷贝
        Object newObj = copyObj(obj);
        //对象脱敏
        for (String fieldName : sensitiveFields)
        {
            setFieldValue(newObj, fieldName, replacement);
        }
        //输出对象内容
        return ToStringBuilder.reflectionToString(newObj);
    }
    
    /**
     * 指定敏感字段后脱敏
     * @param obj
     * @param paramSensitiveFields
     * @return
     */
    public static String reflectionToString(Object obj, String... paramSensitiveFields)
    {
        //对象拷贝
        Object newObj = copyObj(obj);
        //对象脱敏
        for (String fieldName : paramSensitiveFields)
        {
            setFieldValue(newObj, fieldName, replacement);
        }
        //输出对象内容
        return ToStringBuilder.reflectionToString(newObj);
    }
    
    /**
     * 新建一个和原有对象类型相同的新对象，并将属性全部拷贝给新对象
     * @param obj
     * @return
     */
    private static Object copyObj(Object obj)
    {
        try
        {
            Object newObj = obj.getClass().newInstance();
            BeanUtils.copyProperties(newObj, obj);
            return newObj;
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            logger.error(e);
            return null;
        }
    }
    
    /**
     * 设置对象值
     * @param newObj
     * @param fieldName
     * @param replacement
     */
    private static void setFieldValue(Object newObj, String fieldName, String replacement)
    {
        try
        {
            Field f = newObj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object value = f.get(newObj);
            if (value != null)
            {
                //该敏感字段值不为空时才进行脱敏替换。避免将空对象替换成脱敏后的字符串
                f.set(newObj, replacement);
            }
        }
        catch (Exception e)
        {
            //ignore
        }
    }
}
