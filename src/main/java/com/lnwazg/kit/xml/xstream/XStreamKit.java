package com.lnwazg.kit.xml.xstream;

import java.io.File;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;

/**
 * 一个方便的工具类，可以将Object与xml字符串互转
 * @author nan.li
 * @version 2016年7月5日
 */
public class XStreamKit
{
    /**
     * 将对象转为xml
     * @author nan.li
     * @param obj
     * @return
     */
    public static String parseObject2Xml(Object obj)
    {
        if (obj == null)
        {
            return null;
        }
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        return xStream.toXML(obj);
    }
    
    /**
     * 将XML字符串转为对象
     * @author nan.li
     * @param xml
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseXml2Object(String xml, Class<T> clazz)
    {
        if (xml == null)
        {
            return null;
        }
        XStream xs = new XStream();
        xs.processAnnotations(clazz);
        return (T)xs.fromXML(xml);
    }
    
    /**
     * 将XML字符串文件转为对象
     * @author nan.li
     * @param xmlFile
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseXml2Object(File xmlFile, Class<T> clazz)
    {
        if (xmlFile == null)
        {
            return null;
        }
        XStream xs = new XStream();
        xs.processAnnotations(clazz);
        return (T)xs.fromXML(xmlFile);
    }
    
    /**
     * 将XML输入流转为对象
     * @author nan.li
     * @param inputStream
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseXml2Object(InputStream inputStream, Class<T> clazz)
    {
        if (inputStream == null)
        {
            return null;
        }
        XStream xs = new XStream();
        xs.processAnnotations(clazz);
        return (T)xs.fromXML(inputStream);
    }
}
