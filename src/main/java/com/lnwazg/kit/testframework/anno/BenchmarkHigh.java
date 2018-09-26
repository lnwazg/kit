package com.lnwazg.kit.testframework.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 高强度的benchmark测试，10M次循环<br>
 * 控制台输出很耗时严重影响性能对比，因此应该通过纯计算过程去进行对比才能有所发现<br>
 * 10,000,000次循环，一千万次循环
 * @author nan.li
 * @version 2017年2月27日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BenchmarkHigh
{
}
