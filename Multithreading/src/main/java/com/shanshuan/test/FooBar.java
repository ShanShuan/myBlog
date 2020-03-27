package com.shanshuan.test;

/**
 * Created by wangzifeng on 2020/3/26.
 *  交替打印FooBar
 */
public class FooBar {
    private int n;
    private boolean fooTurn = true;
    private Object lock = new Object();

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock){
                if(fooTurn!=true) {lock.wait();};
                printFoo.run();
                fooTurn=false;
                lock.notifyAll();
            }
        }
    }

    public void bar(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (fooTurn != false) {lock.wait();};
                printFoo.run();
                fooTurn = true;
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final FooBar fooBar=new FooBar(3);
        new Thread(new Runnable() {
            public void run() {
                try {
                    fooBar.bar(new Runnable() {
                        public void run() {
                            System.out.println("bar");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    fooBar.foo(new Runnable() {
                        public void run() {
                            System.out.println("foo");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
