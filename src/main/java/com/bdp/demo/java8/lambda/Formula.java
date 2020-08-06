package com.bdp.demo.java8.lambda;

/**
 * 允许在接口中有默认方法实现
 * Java 8 允许我们使用default关键字，为接口声明添加非抽象的方法实现。这个特性又被称为扩展方法：
 * [ˈfɔːmjələ] n. [数] 公式，准则；配方；婴儿食品
 */
public interface Formula {
    double calculate(int a);//ˈkælkjuleɪt]

    /**
     * 在接口 Formula 中，除了抽象方法 caculate 以外，还定义了一个默认方法 sqrt。
     * Formula的实现类只需要实现抽象方法 caculate 就可以了。默认方法 sqrt 可以直接使用。
     */
    default double sqrt(int a) {
        return Math.sqrt(a);//开平方根
    }
}
