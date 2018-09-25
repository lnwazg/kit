package com.lnwazg.kit.security;

import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.lnwazg.kit.servlet.ReqUtils;

/**
 * 页面的JS的安全性检测工具类
 * @author nan.li
 * @version 2016年7月5日
 */
public class PageJsSafeUtils
{
    /**
     * 检查当前页面请求的安全性，安全则返回true，否则返回false<br>
     * @author nan.li
     * @return
     */
    public static boolean checkSecurity(HttpServletRequest request)
    {
        Map<String, String> paramMap = ReqUtils.getParamMap(request);
        for (Map.Entry<String, String> entry : paramMap.entrySet())
        {
            String original = entry.getValue();
            String value = entry.getValue();// 获取参数的值
            if (value != null)
            {
                // Avoid anything between script tags
                Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                
                //清除链接
                scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                
                //清除链接
                scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                
                // Remove any lonesome </script> tag
                scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                
                // Remove any lonesome <script ...> tag
                scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                
                // Avoid eval(...) e­xpressions
                scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                
                // Avoid e­xpression(...) e­xpressions
                scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                
                // Avoid javascript:... e­xpressions
                scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                
                // Avoid vbscript:... e­xpressions
                scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
                value = scriptPattern.matcher(value).replaceAll("");
                
                // Avoid onload= e­xpressions
                scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                
                scriptPattern = Pattern.compile("';.*?(.*?)//", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                
                scriptPattern = Pattern.compile("'.*?;.*?(.*?);.*?'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                value = scriptPattern.matcher(value).replaceAll("");
                
                //非法字符过滤
                value = StringUtils.replace(value, "alert(", "");
                value = StringUtils.replace(value, "document.cookie", "");
                value = StringUtils.replace(value, "'", "");
                value = StringUtils.replace(value, "\"", "");
                value = StringUtils.replace(value, "<", "");
                value = StringUtils.replace(value, ">", "");
                
                if (!original.equals(value))
                {
                    System.out.println(String.format("LiNan's Security Util 在【 %s】检测到页面请求参数含有非法注入脚本，并已将其自动杀死！！！  \n【原有的参数】  %s\n【过滤后参数】  %s\n\n", new Date().toLocaleString(), original, value));
                    return false;
                }
            }
        }
        return true;
    }
}
