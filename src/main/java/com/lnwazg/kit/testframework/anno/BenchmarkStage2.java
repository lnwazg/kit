package com.lnwazg.kit.testframework.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 渐进式增压，压力测试
 * @author nan.li
 * @version 2018年9月26日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BenchmarkStage2
{
}
