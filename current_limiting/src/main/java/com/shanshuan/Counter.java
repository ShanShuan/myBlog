package com.shanshuan;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流算法 计数器
 * Created by wangzifeng on 2020/3/12.
 */
public class Counter {
    /**
     * 最大访问数
     */
    private final int limit=10;
    /**
     * 访问时差
     */
    private  final long timeOut=1000;
    /**
     * 访问时间
     */
    private   long  time;

    /**
     * 当前计数值
     */
    private AtomicInteger atomicInteger=new AtomicInteger(0);

    public boolean isLimit(){
        long now = System.currentTimeMillis();
        if(now-time<timeOut){
            atomicInteger.addAndGet(1);
            return atomicInteger.get()<=limit;
        }else{
            time=now;
            atomicInteger=new AtomicInteger(0);
            return true;
        }

    }

}
