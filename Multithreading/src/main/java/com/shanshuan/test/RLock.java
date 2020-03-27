package com.shanshuan.test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wangzifeng on 2020/3/26.
 */
public class RLock {
     static  ReentrantLock lock=new ReentrantLock(false);
    public static void main(String[] args) {

        new Thread(()->test(),"A").start();
        new Thread(()->test(),"B").start();
        new Thread(()->test(),"C").start();
        new Thread(()->test(),"D").start();
        new Thread(()->test(),"E").start();
        new Thread(()->test(),"F").start();
        new Thread(()->test(),"G").start();
    }

    private static void test() {
        try{
            lock.lock();
            System.out.println(Thread.currentThread().getName()+"获得了锁");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
