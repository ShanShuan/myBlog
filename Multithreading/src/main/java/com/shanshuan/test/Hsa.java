package com.shanshuan.test;

/**
 * Created by wangzifeng on 2020/5/9.
 * 死锁
 */
public class Hsa {
    private static  String A="A";
    private static  String B="B";

    public static void main(String[] args) {
        Thread t1=new Thread(()->{
            synchronized (A){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B){
                    System.out.println("1");
                }
            }
        },"111");

        Thread t2=new Thread(()->{
            synchronized (B){
                synchronized (A){
                    System.out.println("2");
                }
            }
        },"222");
        t1.start();
        t2.start();
    }



}
