package com.lnwazg.kit.robot.turing;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.lnwazg.kit.gson.GsonKit;

/**
 * 机器人工具包
 * @author nan.li
 * @version 2017年8月2日
 */
public class RobotKit
{
    private static final String SERVER_URL = "http://www.tuling123.com/openapi/api";
    
    //图灵网站上的apiKey
    private static String API_KEY = "3ce4a4d8743649198c0e16512f044a06";
    
    //图灵网站上的secret
    private static String SECRET = "f816f607bc96d1e6";
    
    /**
     * 和机器人聊天
     * @author nan.li
     * @param talkContent
     * @return
     */
    public static String talk(String talkContent)
    {
        if (StringUtils.isEmpty(talkContent))
        {
            return "";
        }
        String cmd = talkContent;
        //待加密的json数据
        String data = "{\"key\":\"" + API_KEY + "\",\"info\":\"" + cmd + "\"}";
        //获取时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        //生成密钥
        String keyParam = SECRET + timestamp + API_KEY;
        String key = Md5.MD5(keyParam);
        //加密
        Aes mc = new Aes(key);
        data = mc.encrypt(data);
        //封装请求参数
        Map<String, Object> map = new HashMap<>();
        map.put("key", API_KEY);
        map.put("timestamp", timestamp);
        map.put("data", data);
        //请求图灵api
        String result = PostServer.SendPost(GsonKit.parseObject2String(map), SERVER_URL);
        //{"code":100000,"text":"重庆:周三 8月2日,阴 无持续风向,最低气温30度，最高气温38度"}
        if (StringUtils.isNotEmpty(result))
        {
            JsonObject jsonObject = GsonKit.parseString2JsonObject(result);
            StringBuilder content = new StringBuilder(jsonObject.get("text").getAsString());
            if (jsonObject.get("url") != null)
            {
                content.append(" ").append(jsonObject.get("url").getAsString());
            }
            return content.toString();
        }
        return "";
    }
}
