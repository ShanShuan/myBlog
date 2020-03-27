package com.shanshuan.controller;

import com.shanshuan.bo.RedBag;
import com.shanshuan.mapper.RedBagMapper;
import com.shanshuan.service.RedBageService;
import com.shanshuan.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


/**
 * Created by wangzifeng on 2020/3/26.
 * 并发 抢 红包
 */
@RestController
@Component
public class TestController {
    @Autowired
    RedBagMapper redBagMapper;
    @Autowired
    private RedBageService redBageService;
    @Autowired
    private RedisUtil redisUtil;



    @RequestMapping("/dasd")
    public  String dasd()   {
        redBageService.countDownRedic(1,1);
        return "";
    }


    @RequestMapping("/initRedEnvelopes")
    public  String init()   {
        RedBag red=new RedBag();
        red.setId(1);
        red.setSum(10d);
        red.setCount(10);
        red.setVersion(0);
        redBagMapper.updateByPrimaryKey(red);
        redisUtil.set("1",10);
        return "sucess";
    }



    /**
     * 并发 抢红包
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/test")
    public  String test() throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(100);
        CyclicBarrier cyclicBarrier=new CyclicBarrier(100);
        for (int i = 0; i <100; i++) {
            int finalI = i;
            new Thread(()->{
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
//                redBageService.countDown(finalI,1);//悲观锁
//                redBageService.countDownLg(finalI,1);//乐观锁
                redBageService.countDownRedic(finalI,1);

                countDownLatch.countDown();
            },i+1+"").start();
        }

        countDownLatch.await();
        RedBag redBag = redBagMapper.selectByPrimaryKey(1);
        return redBag.toString();
    }
}
