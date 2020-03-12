package com.shanshuan;

/**
 * 漏桶算法
 * Created by wangzifeng on 2020/3/12.
 */
public class LeakBucket {
    /**
     * 流出速度  每毫秒 1
     */
    private final Double rate=1d;

    /**
     * 漏桶的总容量
     */
    private double limitCount=2000;

    /**
     * 当前的 个数
     */
    private  double  nowSize;


    /**
     * 当前时间
     */
    private long time;


    public boolean isLimit(){
        System.out.println("当前任务 个数"+nowSize);
        long now = System.currentTimeMillis();
        nowSize =  Math.max(0, nowSize - (now - time) * rate*0.01);
        time=now;
        if((nowSize+1)>limitCount){
            return false;
        }else{
            nowSize++;
            return true;
        }
    }

}
