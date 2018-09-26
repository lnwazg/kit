package com.lnwazg.kit.testframework.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 性能压测注解，标记该方法将被压力测试<br>
 * 须指定压力测试的执行次数（方法被循环的次数）
 * @author nan.li
 * @version 2017年2月24日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Benchmark
{
    /**
     * 性能测试的循环次数<br>
     * 必须定义
     * @author nan.li
     * @return
     */
    int value();
}
