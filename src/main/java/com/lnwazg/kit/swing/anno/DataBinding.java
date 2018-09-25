package com.lnwazg.kit.swing.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据绑定
 * @author nan.li
 * @version 2017年8月14日
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBinding
{
    /**
     * 默认的value
     * @author nan.li
     * @return
     */
    String value() default "";
    
    /**
     * 假如这个数据从数据源取出来为空，那么显示该替代值
     * @author nan.li
     * @return
     */
    String dataNullShowFix() default "";
}
