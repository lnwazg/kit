package com.lnwazg.kit.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ObjectUtils;

import com.lnwazg.kit.log.Logs;

public class Maps
{
    /**
     * 快速生成一个map，entry保持插入顺序
     * @author nan.li
     * @param objects
     * @return
     */
    public static Map<String, Object> asMap(Object... objects)
    {
        return asLinkedHashMap(objects);
    }
    
    /**
     * 快速生成一个有序表（保持插入时的顺序）
     * @author nan.li
     * @param objects
     * @return
     */
    public static Map<String, Object> asLinkedHashMap(Object... objects)
    {
        Map<String, Object> ret = new LinkedHashMap<>();
        if (objects != null && objects.length > 0 && objects.length % 2 == 0)
        {
            for (int i = 0; i + 1 < objects.length; i += 2)
            {
                ret.put(ObjectUtils.toString(objects[i]), objects[i + 1]);
            }
        }
        else
        {
            Logs.w("参数个数非法！");
        }
        return ret;
    }
    
    /**
     * 快速生成一个map，entry保持插入顺序，键值类型都是Object，更加通用
     * @author nan.li
     * @param objects
     * @return
     */
    public static Map<Object, Object> asObjectMap(Object... objects)
    {
        Map<Object, Object> ret = new LinkedHashMap<>();
        if (objects != null && objects.length > 0 && objects.length % 2 == 0)
        {
            for (int i = 0; i + 1 < objects.length; i += 2)
            {
                ret.put(objects[i], objects[i + 1]);
            }
        }
        else
        {
            Logs.w("参数个数非法！");
        }
        return ret;
    }
    
    /**
     * 快速生成一个map，entry保持插入顺序，键值类型都是String
     * @author nan.li
     * @param objects
     * @return
     */
    public static Map<String, String> asStrMap(Object... objects)
    {
        Map<String, String> ret = new LinkedHashMap<>();
        if (objects != null && objects.length > 0 && objects.length % 2 == 0)
        {
            for (int i = 0; i + 1 < objects.length; i += 2)
            {
                ret.put(ObjectUtils.toString(objects[i]), ObjectUtils.toString(objects[i + 1]));
            }
        }
        else
        {
            Logs.w("参数个数非法！");
        }
        return ret;
    }
    
    public static HashMap<String, String> asStrHashMap(Object... objects)
    {
        return (HashMap<String, String>)asStrMap(objects);
    }
    
    /**
     * 快速生成一个map，entry按字母表顺序顺序，键值类型都是String
     * @author nan.li
     * @param objects
     * @return
     */
    public static TreeMap<Object, Object> asTreeMap(Object... objects)
    {
        TreeMap<Object, Object> ret = new TreeMap<>();
        if (objects != null && objects.length > 0 && objects.length % 2 == 0)
        {
            for (int i = 0; i + 1 < objects.length; i += 2)
            {
                ret.put(objects[i], objects[i + 1]);
            }
        }
        else
        {
            Logs.w("参数个数非法！");
        }
        return ret;
    }
    
    public static TreeMap<String, String> asStrTreeMap(Object... objects)
    {
        TreeMap<String, String> ret = new TreeMap<>();
        if (objects != null && objects.length > 0 && objects.length % 2 == 0)
        {
            for (int i = 0; i + 1 < objects.length; i += 2)
            {
                ret.put(ObjectUtils.toString(objects[i]), ObjectUtils.toString(objects[i + 1]));
            }
        }
        else
        {
            Logs.w("参数个数非法！");
        }
        return ret;
    }
    
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }
    
    public static boolean isEmpty(Map<?, ?> map)
    {
        if (map == null || map.size() == 0)
        {
            return true;
        }
        return false;
    }
    
}
