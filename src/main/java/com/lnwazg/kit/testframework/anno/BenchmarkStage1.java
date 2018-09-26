package com.lnwazg.kit.testframework.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 渐进式增压，压力测试<br>
 * 五档可调，压力测试<br>
 * 每一档都是上一档的十倍的量
 * @author nan.li
 * @version 2018年9月26日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BenchmarkStage1
{
}
