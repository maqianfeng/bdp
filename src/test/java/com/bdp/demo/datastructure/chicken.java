package com.bdp.demo.datastructure;

import com.bdp.demo.DemoApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 公鸡5元，母鸡3元，小鸡1元3只 100元买100只有多少种买法
 * <p>
 * x+y+z=100;
 * 5x+3y+z/3=100;
 * 最终求解：7x+4y=100;
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class chicken {
    @Test
    public void chicks() {
        int x, y, z;
        for (x = 1; x <= 20; x++) {
            for (y = 1; y <= 33; y++) {
                z = 100 - x - y;
                if (5 * x + 3 * y + z / 3 == 100) {
                    System.err.println("x:" + x + ",y:" + y + ",z:" + z);
                }
            }
        }
    }

    /**
     * 最大公约数
     */
    @Test
    public void commonfactor() {
        int m=10,n=6,r;
        while (true) {
            r = m % n;
            if (r == 0) {
                System.err.println(n);
                return;
            }
            m = n;
            n = r;
        }
    }


}
