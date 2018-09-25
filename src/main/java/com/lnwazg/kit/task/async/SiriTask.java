package com.lnwazg.kit.task.async;

/**
 * Siri任务接口
 * @author nan.li
 * @version 2018年2月5日
 */
public interface SiriTask
{
    /**
     * 该接口的优势在于：支持任意数量、任意类型的参数的传递<br>
     * 这种用法跟js的用法非常类似，因此可以实现很高的框架灵活度
     * @author nan.li
     * @param params
     */
    void execute(Object... params);
}
