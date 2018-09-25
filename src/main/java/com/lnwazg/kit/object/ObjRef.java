package com.lnwazg.kit.object;

/**
 * 对象的引用
 * @author nan.li
 * @version 2017年7月22日
 */
public class ObjRef<T>
{
    T value;
    
    /**
     * 构造函数 
     * @param value
     */
    public ObjRef(T value)
    {
        this.value = value;
    }
    
    /**
     * 获取这个对象的值
     * @author nan.li
     * @return
     */
    public T get()
    {
        return value;
    }
}
