package com.lnwazg.kit.handlerseq;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将方法标记为一项可执行任务
 * @author nan.li
 * @version 2018年9月20日
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Task
{

}
