package com.lnwazg.kit.reflect;

import com.lnwazg.kit.javac.in.memory.InMemoryJavaCompiler;

/**
 * 热编译工具
 * @author nan.li
 * @version 2017年8月16日
 */
public class HotCodeKit extends InMemoryJavaCompiler
{
    public static void main(String[] args)
        throws Exception
    {
        StringBuffer sourceCode = new StringBuffer();
        
        sourceCode.append("package org.mdkt;\n");
        sourceCode.append("public class HelloClass {\n");
        
        //方法一
        sourceCode.append("   public String hello() { return \"hello\"; }");
        //方法二
        sourceCode.append("   public String helloParam(String aaa) { return \"hello\" + aaa ; }");
        
        sourceCode.append("}");
        
        //        Class<?> helloClass = compile("org.mdkt.HelloClass", sourceCode.toString());
        //        Object object = ClassKit.newInstance(helloClass);
        //        System.out.println((Object)ClassKit.invokeMethod(object, "hello"));
        System.out.println(compileSourceCodeStrAndInvokeMethod("org.mdkt.HelloClass", sourceCode.toString(), "hello"));
        System.out.println(compileSourceCodeStrAndInvokeMethod("org.mdkt.HelloClass", sourceCode.toString(), "helloParam", "bbbccc"));
        
        compileSourceCodeFileAndInvokeMethod("com.lnwazg.kit.reflect.TestMain", "C:\\TestMain.java", "print");
    }
    
}