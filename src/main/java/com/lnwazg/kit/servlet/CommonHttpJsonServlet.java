package com.lnwazg.kit.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lnwazg.kit.gson.GsonKit;

/**
 * 通用型servlet<br>
 * 仅支持POST提交，请求头必须加上method方法，请求以及响应都是json格式
 * @author nan.li
 * @version 2018年12月20日
 */
public class CommonHttpJsonServlet
{
    private static final Logger logger = LoggerFactory.getLogger(CommonHttpJsonServlet.class);
    
    private static final String CONTENT_TYPE_JSON = "application/json";
    
    private Object service;
    
    /**
    * 默认字符集
    */
    public static String DEFAULT_ENCODING = "UTF-8";
    
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        logger.info("CommonHttpJsonServlet.handleRequest() begin...");
        
        if (!"POST".equals(request.getMethod()))
        {
            throw new ServletException("CommonHttpJsonServlet only supports POST requests");
        }
        
        //响应内容类型
        response.setContentType(CONTENT_TYPE_JSON);
        String resultStr = null;
        try
        {
            String invokeMethodName = request.getHeader("method");
            logger.info("invokeMethodName=" + invokeMethodName);
            
            if (StringUtils.isBlank(invokeMethodName))
            {
                resultStr = "请求头中未定义要调用的method方法";
            }
            else
            {
                //将请求流转为json
                String requestJson = parseRequest2Str(request);
                logger.info("requestJson=" + requestJson);
                
                if (StringUtils.isBlank(requestJson))
                {
                    resultStr = "Post消息体json为空";
                }
                else
                {
                    //定位到方法，进行调用
                    Method method = guessUniqueMethod(service.getClass(), invokeMethodName);
                    if (method == null)
                    {
                        logger.info("method cannot be found!");
                        resultStr = "已发布的服务中不包含" + invokeMethodName + "方法，因此无法调用";
                    }
                    else
                    {
                        logger.info("method found!");
                        Class<?>[] parameterClassArray = method.getParameterTypes();
                        Object paramObj = null;
                        Object resultObj = null;
                        if (parameterClassArray == null || parameterClassArray.length == 0)
                        {
                            logger.info("begin to invoke non param method, methodName=" + invokeMethodName);
                            //无参的方法，可以调用
                            resultObj = method.invoke(service);
                            //将调用结果对象转为json
                            resultStr = GsonKit.parseObject2String(resultObj);
                        }
                        else if (parameterClassArray.length == 1)
                        {
                            logger.info("begin to invoke 1 param method");
                            //只有一个参数的方法，可以调用
                            paramObj = GsonKit.parseString2Object(requestJson, parameterClassArray[0]);
                            logger.info("begin to invoke methodName=" + invokeMethodName + " paramObj=" + paramObj);
                            resultObj = method.invoke(service, paramObj);
                            //将调用结果对象转为json
                            resultStr = GsonKit.parseObject2String(resultObj);
                        }
                        else
                        {
                            logger.info("method param length is more than 1, please modify it!");
                            //超过一个参数，不可调用
                            resultStr = "方法" + invokeMethodName + "入参超过一个，因此无法进行json解析转换，请联系DDP研发修改！";
                        }
                    }
                }
            }
            
            logger.info("resultStr=" + resultStr);
            //输出
            response.setCharacterEncoding(DEFAULT_ENCODING);
            response.setContentType(CONTENT_TYPE_JSON);
            response.getWriter().write(resultStr);
            response.getWriter().flush();
        }
        catch (Exception e)
        {
            logger.error("CommonHttpJsonServlet invocation failed", e);
        }
        logger.info("CommonHttpJsonServlet.handleRequest() end!");
    }
    
    /**
     * 将请求数据转为对象
     * @param httpRequest
     * @return
     */
    public static String parseRequest2Str(HttpServletRequest httpRequest)
    {
        String line = null;
        ServletInputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            is = httpRequest.getInputStream();
            isr = new InputStreamReader(is, DEFAULT_ENCODING);
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
                sb.append("\n");
            }
        }
        catch (IOException e)
        {
            logger.error("parseRequest2Str exception", e);
        }
        finally
        {
            try
            {
                if (null != is)
                    is.close();
                if (null != isr)
                    isr.close();
                if (null != br)
                    br.close();
            }
            catch (Exception e)
            {
                logger.error("io close exception", e);
            }
        }
        return sb.toString();
    }
    
    /**
     * 猜唯一方法<br>
     * 之所以称为“猜唯一方法”，是在你对这个类的方法除了方法名你知道之外、方法的参数类型和返回值类型均一无所知的情况下，获取这个类的唯一方法的最后一招<br>
     * 调用此方法的clazz不能重载方法，否则会出现方法匹配错误的问题！
     * @author nan.li
     * @param clazz
     * @param methodName
     * @return
     */
    public Method guessUniqueMethod(Class<?> clazz, String methodName)
    {
        if (clazz == null || StringUtils.isEmpty(methodName))
        {
            return null;
        }
        //只取公有方法，私有方法将被过滤掉
        Method[] methods = clazz.getMethods();
        for (Method method : methods)
        {
            if (method.getName().equals(methodName))
            {
                return method;
            }
        }
        return null;
    }
    
    public void setService(Object service)
    {
        this.service = service;
    }
}
