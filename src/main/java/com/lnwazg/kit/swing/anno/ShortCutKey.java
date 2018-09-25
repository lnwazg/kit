package com.lnwazg.kit.swing.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 快捷键定义注解，并且该注解是可重复定义的
 * @author nan.li
 * @version 2017年8月14日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ShortCutKeys.class)
public @interface ShortCutKey
{
    /**
     * 绑定具体的键位，例如：KeyEvent.VK_F1、KeyEvent.VK_1
     * @author nan.li
     * @return
     */
    int value();
}
