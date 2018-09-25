package com.lnwazg.kit.anno;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用型注解，可以作为注释来使用<br>
 * 跟注释的区别在于，该注解可以被框架读取并利用
 * @author nan.li
 * @version 2017年12月10日
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Anno
{
    String value();
}
