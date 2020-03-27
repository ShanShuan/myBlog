package com.shanshuan.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangzifeng on 2020/3/26.
 *按顺序打印
 */
public class One {

    AtomicInteger first;
    AtomicInteger secord;
    public One() {
         first=new AtomicInteger(0);
         secord=new AtomicInteger(0);

    }

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        first.incrementAndGet();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        while(first.get()!=1){

        }
        printSecond.run();
        secord.incrementAndGet();

    }

    public void third(Runnable printThird) throws InterruptedException {
        while(secord.get()!=1){

        }
        printThird.run();
    }

    public static void main(String[] args) throws InterruptedException {
        final One one=new One();
        final Runnable runnableone=new Runnable() {
            public void run() {
                System.out.println("one");
            }
        };

        final Runnable runnabletwo=new Runnable() {
            public void run() {
                System.out.println("two");
            }
        };

        final Runnable runnableThree=new Runnable() {
            public void run() {
                System.out.println("three");
            }
        };
        new Thread(new Runnable() {
            public void run() {
                try {
                    one.third(runnableThree);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                try {
                    one.second(runnabletwo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                try {
                    one.first(runnableone);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


}
