package com.shanshuan;

/**
 * 令牌桶
 * Created by wangzifeng on 2020/3/12.
 */
public class TokenBucket {
  private  final long limitCount=10;
  private  long time;
  private long rate=1;//令牌桶 放入速度
  private long nowSize;



    public boolean isLimit(){
        long now = System.currentTimeMillis();
        long needPutCount = (now - time) * rate/1000;
        nowSize=Math.min(limitCount,nowSize+needPutCount);
        time=now;
        if(nowSize<1){
            return false;
        }else{
            nowSize-=1;
            return true;
        }
    }
}
