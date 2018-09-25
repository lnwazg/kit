package com.lnwazg.kit.singleton;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.lnwazg.kit.reflect.ClassKit;

/**
 * 实例管理器<br>
 * 支持单例模式，也支持单Class多实例模式
 * @version 2016年4月15日
 */
public class BeanMgr
{
    //多实例模式===================================================================================================
    /**
     * 多实例类-对象映射表<br>
     * key: className+InstanceName
     * value: the Instance Object
     */
    private static Map<String, Object> MultiInstanceClazz2ObjectMap = new HashMap<>();
    
    /**
     * 存储该类的加了别名的实例
     * @author nan.li
     * @param t
     * @param beanName
     */
    public static <T> void put(T t, String beanName)
    {
        String className = t.getClass().getName();
        String key = String.format("%s-%s", className, beanName);
        MultiInstanceClazz2ObjectMap.put(key, t);
    }
    
    public static <T> void s(T t, String beanName)
    {
        put(t, beanName);
    }
    
    /**
     * 存储该类的加了别名的实例
     * @author nan.li
     * @param t
     * @param beanName
     */
    public static void put(Class<?> clazz, String beanName, Object object)
    {
        String className = clazz.getName();
        String key = String.format("%s-%s", className, beanName);
        MultiInstanceClazz2ObjectMap.put(key, object);
    }
    
    public static void s(Class<?> clazz, String beanName, Object object)
    {
        put(clazz, beanName, object);
    }
    
    /**
     * 查找某个Class对应的单例实例<br>
     * 仅查询某个key clazz是否有值，不做额外的实例化操作
     * @author nan.li
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T query(Class<T> clazz, String beanName)
    {
        String className = clazz.getName();
        String key = String.format("%s-%s", className, beanName);
        return (T)MultiInstanceClazz2ObjectMap.get(key);
    }
    
    public static <T> T q(Class<T> clazz, String beanName)
    {
        return query(clazz, beanName);
    }
    
    /**
     * 查找某个Class对应的单例实例<br>
     * 仅查询某个key clazz是否有值，不做额外的实例化操作
     * @author nan.li
     * @param clazz
     * @return
     */
    public static <T> T get(Class<T> clazz, String beanName)
    {
        return query(clazz, beanName);
    }
    
    public static <T> T g(Class<T> clazz, String beanName)
    {
        return get(clazz, beanName);
    }
    
    //单例模式===================================================================================================
    /**
     * 类-实例 映射表，单例模式<br>
     * 一个类只能映射为一个实例对象
     */
    private static Map<Class<?>, Object> SingletonClazz2ObjectMap = new HashMap<>();
    
    /**
     * 存储一个类的实例对象<br>
     * 根据实例名自动推测出所属的类<br>
     * 注意：此方法可能不适用于动态代理工具生产出来的实例的自动注入！<br>
     * 因为动态代理工具生产出来的实例一般是XXXProxy$xxx这样的内部类，因此其Class实际上已经是动态代理类的Class，而非原有Class<br>
     * 因此，对于动态代理类的注册，应该使用:put(Class<?> clazz, Object object)方法，去手动指定Class对象
     * @param t
     */
    public static <T> void put(T t)
    {
        SingletonClazz2ObjectMap.put(t.getClass(), t);
    }
    
    public static <T> void set(T t)
    {
        put(t);
    }
    
    public static <T> void s(T t)
    {
        put(t);
    }
    
    /**
     * 存储一个类的实例对象<br>
     * 同样是普遍适用的，但是该方法不限制实例的泛型类型，可以任意指定对象进行注入，包括动态代理工具生产的对象！<br>
     * 这个方法将比put(Class<T> clazz, T t)更强大给力！
     * @author nan.li
     * @param clazz
     * @param object
     */
    public static void put(Class<?> clazz, Object object)
    {
        SingletonClazz2ObjectMap.put(clazz, object);
    }
    
    public static void s(Class<?> clazz, Object object)
    {
        put(clazz, object);
    }
    
    /**
     * 查找某个Class对应的单例实例<br>
     * 仅查询某个key clazz是否有值，不做额外的实例化操作
     * @author nan.li
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T query(Class<T> clazz)
    {
        return (T)SingletonClazz2ObjectMap.get(clazz);
    }
    
    public static <T> T q(Class<T> clazz)
    {
        return query(clazz);
    }
    
    /**
     * 查找某个Clazz的实例并返回<br>
     * 若查不到，则调用该Class的默认构造函数，自动实例化一个该Class的默认实例
     * @author Administrator
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> clazz)
    {
        T t = (T)SingletonClazz2ObjectMap.get(clazz);
        if (t == null)
        {
            //为空，则返回默认的实例
            try
            {
                t = clazz.newInstance();
                put(t);
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return t;
    }
    
    public static <T> T g(Class<T> clazz)
    {
        return get(clazz);
    }
    
    /**
     * 获取某个包下面的某个类的单例实例对象
     * @author Administrator
     * @param packageName
     * @param clazzName
     * @return
     */
    public static Object getBeanByClassName(String packageName, String clazzName)
    {
        String clazzFullName = String.format("%s.%s", packageName, clazzName);
        Class<?> clazz = ClassKit.forName(clazzFullName);
        return get(clazz);
    }
    
    //================================================================================================
    
    /**
     * 将类的所有加了@Resource注解的字段根据字段类型自动注入对应的参数对象，然后将注入好的对象注册到单例表中
     * @author nan.li
     * @param clazz
     * @param objects
     */
    public static <T> void injectAndPut(Class<T> clazz, Object... objects)
    {
        try
        {
            T t = clazz.newInstance();
            injectByTypeAndAnno(t, objects);
            put(t);
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 为某个对象注入一堆对象
     * @author nan.li
     * @param t
     * @param objects
     */
    public static <T> void injectAndPut(T t, Object... objects)
    {
        injectByTypeAndAnno(t, objects);
        put(t);
    }
    
    /**
     * 根据类型以及注解，进行注入操作
     * @author nan.li
     * @param t
     * @param objects
     */
    @SuppressWarnings("unchecked")
    private static <T> void injectByTypeAndAnno(T t, Object... objects)
    {
        if (objects == null || objects.length == 0)
        {
            return;
        }
        Class<T> clazz = (Class<T>)t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(Resource.class))
            {
                //标明该字段需要被注入
                for (Object object : objects)
                {
                    Class<?> fieldType = field.getType();
                    //如果加了@Resource注解的字段是某个object的接口，那么就将该object注入到该字段里面
                    if (fieldType.isAssignableFrom(object.getClass()))
                    {
                        //两者一致，则执行注入操作！
                        try
                        {
                            field.setAccessible(true);
                            field.set(t, object);
                        }
                        catch (IllegalArgumentException | IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
