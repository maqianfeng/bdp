package com.bdp.demo.datastructure;

/**
 * 递归
 */
public class Solution {
    //我们看看经常涉及到 递归 的 算法题（来源leetcode）：
    //算法题：实现 pow(x, n) ，即计算 x 的 n 次幂函数。-100.0 < x < 100.0，n 是 32 位有符号整数，其数值范围是 [−2^31, 2^31 − 1]
    //输入: 2.00000, 10
    //输出: 1024.00000
    //解题思路：
    //方法一：
    //暴力解法，直接写一个循环让n个x相乘嘛，当然了这种方式就没啥技术含量了，时间复杂度O(1)，代码省略了。
    //方法二：
    //基于递归原理，很容易就找出递推公式 f(n)=x*f(n-1)，再找出递归停止条件即n==0或1的情况就可以了。
    // 不过稍微需要注意的是，因为n的取值可以是负数，所以当n小于0的时候，就要取倒数计算。代码如下：
    public double myPow(double x, int n) {
        if (n == 0) return 1;
        if (n == 1) return x;
        if (n < 0) return 1 / (x * myPow(x, Math.abs(n) - 1));
        return x * myPow(x, n - 1);
    }

    //这个方法其实也有问题，当n的数值过大时，会堆栈溢出的，看来也是不最佳解，继续往下看。
    //方法三：
    //利用分治的思路，将n个x先分成左右两组，分别求每一组的值，然后再将两组的值相乘就是总值了。
    //即 x的n次方等于 x的n/2次方 乘以x的n/2次方。以此类推，左右两组其实还可以分别各自继续往下分组，就是一个递推思想了。
    //但是这里需要考虑一下当n是奇数的情况，做一个特殊处理即可，代码如下：

    public double myPow2(double x, int n) {
    //如果n是负数，则改为正数，但把x取倒数
        if (n < 0) {
            n = -n;
            x = 1 / x;
        }
        return pow(x, n);

    }

    private double pow(double x, int n) {
        if (n == 0) return 1;
        if (n == 1) return x;
        double half = pow(x, n / 2);
        //偶数个
        if (n % 2 == 0) {
            return half * half;
        }
        //奇数个
        return half * half * x;
    }
    //这种方法的时间复杂度就是O(logN)了。

    public static void main(String[] args) {
        int m=10,n=6,r;
        while (true) {
            r = m % n;
            if (r == 0) {
                System.err.println(n);;
            }
            m = n;
            n = r;
        }
    }
}
