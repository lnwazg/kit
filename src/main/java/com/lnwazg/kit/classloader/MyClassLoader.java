package com.lnwazg.kit.classloader;

import java.io.FileInputStream;

/**
 * 自定义类加载器
 * @author nan.li
 * @version 2018年10月17日
 */
public class MyClassLoader extends ClassLoader
{
    /**
     * 类路径
     */
    private String classPath;
    
    public MyClassLoader(String classPath)
    {
        this.classPath = classPath;
    }
    
    /**
     * 需要手动实现findClass()方法
     */
    @Override
    protected Class<?> findClass(String name)
        throws ClassNotFoundException
    {
        try
        {
            //第一步，加载类的字节码
            byte[] data = loadByte(name);
            //第二步，根据字节码定义类
            return defineClass(name, data, 0, data.length);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }
    
    /**
     * 加载类的字节码
     * @author nan.li
     * @param name
     * @return
     * @throws Exception
     */
    private byte[] loadByte(String name)
        throws Exception
    {
        name = name.replaceAll("\\.", "/");
        FileInputStream fis = new FileInputStream(classPath + "/" + name + ".class");
        int len = fis.available();
        byte[] data = new byte[len];
        fis.read(data);
        fis.close();
        return data;
    }
}