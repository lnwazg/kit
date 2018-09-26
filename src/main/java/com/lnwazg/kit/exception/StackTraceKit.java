package com.lnwazg.kit.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.lnwazg.kit.io.StreamUtils;

public class StackTraceKit
{
    /**
     * 将堆栈转换成String
     * @author nan.li
     * @param e
     * @return
     */
    public static String getStackTraceString(Throwable e)
    {
        if (e != null)
        {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            printWriter.flush();
            StreamUtils.close(printWriter);
            String stackTrace = stringWriter.toString();
            return stackTrace;
        }
        return null;
    }
}
