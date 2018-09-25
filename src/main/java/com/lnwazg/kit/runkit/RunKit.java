package com.lnwazg.kit.runkit;

/**
 * 打出方法运行时信息
 * @author nan.li
 * @version 2017年8月6日
 */
public class RunKit
{
    /**
     * 打出正在运行的类全路径
     * @author nan.li
     * @return
     */
    public static String getClassName()
    {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        return stacks[1].getClassName();
        //com.lnwazg.kit.runkit.RunKit
    }
    
    /**
     * 打出正在运行的方法名
     * @author nan.li
     * @return
     */
    public static String getMethodName()
    {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        return stacks[1].getMethodName();
        //main
    }
    
    /**
     * 打出正在运行的代码的所在行号
     * @author nan.li
     * @return
     */
    public static int getLineNum()
    {
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        return stacks[1].getLineNumber();
        //50   -> 对应着这一行   System.out.println(RunKit.getLineNum());
    }
    
    public static void main(String[] args)
    {
        System.out.println(RunKit.getClassName());
        System.out.println(RunKit.getMethodName());
        System.out.println(RunKit.getLineNum());
    }
}
