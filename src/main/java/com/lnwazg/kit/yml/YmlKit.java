package com.lnwazg.kit.yml;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.ho.yaml.Yaml;

/**
 * @author nan.li
 * @version 2018年8月4日
 */
public class YmlKit
{
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args)
    {
        try
        {
            HashMap<String, Map> map = Yaml.loadType(YmlKit.class.getResourceAsStream("application.yml"), HashMap.class);
            System.out.println(map);
            System.out.println(map.get("spring"));
            System.out.println(map.get("spring").get("datasource"));
            System.out.println(((Map)(map.get("spring").get("datasource"))).get("password"));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
}
