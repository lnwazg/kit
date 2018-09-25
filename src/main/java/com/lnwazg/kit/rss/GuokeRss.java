package com.lnwazg.kit.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

/**
 * RSS解析工具
 * @author nan.li
 * @version 2017年7月12日
 */
public class GuokeRss
{
    /**
     * 获取当前的RSS列表
     * @author nan.li
     * @return
     */
    public static List<String> getNowRssList()
    {
        try
        {
            XML xml = new XMLDocument(new URL("https://cn.engadget.com/rss.xml"));
            List<String> titles = xml.xpath("//channel/item/title/text()");
            return titles;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    public static void main(String[] args)
    {
        System.out.println(getNowRssList());
    }
}
