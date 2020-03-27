package com.shanshuan.test;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangzifeng on 2020/3/26.
 * 打印零与奇偶数
 *
 * 3--》010203
 * 5--》0102030405
 *
 */
public class ZeroEvenOdd {
    private int  n;

    static class IntConsumer{
        public int  accept( int x){
            return x;
        }

    }

    private Object lock;
    private AtomicInteger integer;
    public ZeroEvenOdd(int n) {
        this.n = n;
        integer=new AtomicInteger(0);
        lock=new Object();
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (integer.get() % 2 != 0) {
//                    System.out.println(integer.get()+"等待  零");
                    lock.wait();
                }
//                System.out.println("零 "+integer.get());
                System.out.println("0");
                integer.incrementAndGet();
                lock.notifyAll();
            }
        }
    }


    //偶数
    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n/2; i++) {
            synchronized (lock) {
                while ((integer.get()+5 )%4 != 0) {
//                    System.out.println(integer.get()+"等待 偶数");
                    lock.wait();
                }
//                System.out.println("偶数 "+integer.get());
                System.out.println((integer.get()+1)/2);
                integer.incrementAndGet();
                lock.notifyAll();
            }
        }

    }

    //奇数
    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <=(n+1)/2; i++) {
            synchronized (lock) {
                while ((integer.get()+3 )% 4 != 0) {
//                    System.out.println(integer.get()+"等待 奇数 ");
                    lock.wait();
                }
//                System.out.println("奇数 "+integer.get());
                System.out.println((integer.get()+1)/2);
                integer.incrementAndGet();
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        final ZeroEvenOdd a=new ZeroEvenOdd(8);
        final IntConsumer o=new IntConsumer();
        final IntConsumer t=new IntConsumer();
        final IntConsumer th=new IntConsumer();
        new Thread(new Runnable() {
            public void run() {
                try {
                    a.odd(th);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            public void run() {
                try {
                    a.even(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            public void run() {
                try {
                    a.zero(o);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
