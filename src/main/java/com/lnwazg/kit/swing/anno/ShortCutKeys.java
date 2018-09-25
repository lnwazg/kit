package com.lnwazg.kit.swing.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多重快捷键定义<br>
 * 当一个字段上定义了多个@ShortCutKey，那么这个字段上的注解会自动变成一个@ShortCutKeys，这个过程是由编译器自动转换的<br>
 * 此时，field.isAnnotationPresent(ShortCutKeys.class)为true，而field.isAnnotationPresent(ShortCutKey.class)为false<br>
 * 这个现象是由多重枚举定义的内部实现决定的
 * @author nan.li
 * @version 2017年8月14日
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShortCutKeys
{
    ShortCutKey[] value() default {};
}
