package org.example.feature.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 随便自定义的一个注解
 *
 * @author: jinyun
 * @date: 2021/2/20-14:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Map {
    String value();
}
