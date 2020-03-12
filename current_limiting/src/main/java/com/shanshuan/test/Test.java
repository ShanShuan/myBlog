package com.shanshuan.test;

import com.shanshuan.Counter;
import com.shanshuan.LeakBucket;
import com.shanshuan.TokenBucket;

import java.sql.Time;
import java.util.Date;

/**
 * Created by wangzifeng on 2020/3/12.
 */
public class Test {
    public static void main(String[] args) {
        Counter counter=new Counter();
        LeakBucket leakBucket=new LeakBucket();
        TokenBucket tokenBucket=new TokenBucket();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
//                    System.out.println("是否可以访问"+counter.isLimit());
//                    System.out.println("是否可以访问"+leakBucket.isLimit());
                    System.out.println("是否可以访问"+tokenBucket.isLimit());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
