package com.bdp.demo.java8.Annotations;

import java.lang.annotation.Repeatable;

/**
 *  只要在前面加上注解名：@Repeatable
 *  Java 8 允许我们对同一类型使用多重注解，
 */
@Repeatable(Hints.class)
public @interface Hint {
    String value();
}
