package com.shanshuan.service;

import com.shanshuan.bo.RedBag;
import com.shanshuan.mapper.RedBagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzifeng on 2020/3/27.
 */
@Service
public class RedBageService {
    @Autowired
    RedBagMapper redBagMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 悲观锁
     * @param finalI
     */
    @Transactional
    public  void countDown(int finalI,int id){
        RedBag redBag=redBagMapper.selectForupdateByid(id);
        if(redBag.getSum()>0) {
            System.out.println("线程"+ (finalI +1)+"已经抢到红包");
            redBagMapper.updateReduce(redBag.getId());
        }else{
            System.out.println("线程"+ (finalI +1)+"没有抢到红包");
        }
    }

    /**
     * 乐观
     * @param finalI
     */
    public  void countDownLg(int finalI,int id){
        while (true) {
            RedBag redBag = redBagMapper.selectByPrimaryKey(id);
            if (redBag.getSum() > 0) {//红包还有
                System.out.println("线程"+ (finalI +1)+"可以抢红包");
                int count=redBagMapper.updateReduceByVersion(redBag.getId(),redBag.getVersion());
                if(count!=0){
                    System.out.println("线程"+ (finalI +1)+"抢红包 成功");
                    return;
                }
                System.out.println("线程"+ (finalI +1)+"抢红包 并发 继续抢");
            } else {
                System.out.println("没有红包了");
                return;
            }
        }
    }


    public void countDownRedic(int finalI, int i) {
        DefaultRedisScript<List> getRedisScript = new DefaultRedisScript<List>();
        getRedisScript.setResultType(List.class);
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/LimitLoadTimes.lua")));
        List<String> keyList = new ArrayList();
        keyList.add(i+"");
        List<String> vauleList = new ArrayList();
        List execute = redisTemplate.execute(getRedisScript, keyList, vauleList);
        if(execute.size()>0){
            if((Long)execute.get(0)==1){
                System.out.println("抢红包成功");
            }else{
                System.out.println("抢红包失败");
            }
        }
    }
}
