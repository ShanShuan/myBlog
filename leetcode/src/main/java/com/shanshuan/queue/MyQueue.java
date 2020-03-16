package com.shanshuan.queue;

import java.util.ArrayList;
import java.util.List;

/**
 * 最简单的队列实现
 * Created by wangzifeng on 2020/3/16.
 * 上面的实现很简单，但在某些情况下效率很低。 随着起始指针的移动，浪费了越来越多的空间。 当我们有空间限制时，这将是难以接受的
 */
public class MyQueue {
    private List<Integer> data;
    private int start;

    public MyQueue() {
        data=new ArrayList<Integer>();
        start=0;
    }

    public boolean enQueue(int x) {
        data.add(x);
        return true;
    }

    public boolean deQueue() {
        if (isEmpty() == true) {
            return false;
        }
        start++;
        return true;
    }

    public int getNow() {
        return data.get(start);
    }

    private boolean isEmpty() {
        return data.size()<=start;
    }

    public static void main(String[] args) {
        MyQueue myQueue=new MyQueue();
        myQueue.enQueue(1);
        System.out.println(myQueue.getNow()+"");
        myQueue.enQueue(2);
        myQueue.enQueue(3);
        myQueue.enQueue(4);
        myQueue.enQueue(5);
        myQueue.enQueue(6);
        myQueue.deQueue();
    }
}
