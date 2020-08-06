package com.bdp.demo.java8.Annotations;

/**
 * 定义一个包装注解，它包括了一个实际注解的数组
 */
public @interface Hints {
    Hint[] value();
}
