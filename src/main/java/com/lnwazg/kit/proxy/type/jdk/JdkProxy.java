package com.lnwazg.kit.proxy.type.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK代理工具
 * @author nan.li
 * @version 2018年12月19日
 */
public class JdkProxy
{
    
    /**
     * 代理一个接口的实现类，返回JDK代理类<br>
     * @author nan.li
     * @param clazz
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T proxy(Class<T> clazz, T object)
    {
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, new InvocationHandler()
        {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable
            {
                return method.invoke(object, args);
            }
        });
    }
}
