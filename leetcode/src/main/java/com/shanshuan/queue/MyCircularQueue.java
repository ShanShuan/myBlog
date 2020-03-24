package com.shanshuan.queue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by wangzifeng on 2020/3/16.
 */
public class MyCircularQueue {
    private int[] data;
    private int tail;//尾
    private int head;//头
    private int size;

    public MyCircularQueue(int size) {
        data=new int[size];
        tail=-1;
        head=-1;
        this.size=size;
    }

    public boolean enQueue(int value) {

        if (isEmpty() == true) {
            head = 0;
        }
        if (isFull() == true) {
            return false;
        }
        data[(head+1)%size]=value;
        tail++;
        return true;
    }



    public boolean deQueue() {
        if(isEmpty()){
            return false;
        }
        if(tail==head){
            tail=-1;
            head=-1;
            return true;
        }
        head=(head+1)%size;
        return true;
    }


    private boolean isFull() {
        if(tail==-1) return false;
        return (tail+1)%size==head;
    }

    private boolean isEmpty() {
        return head==-1;
    }

    public int Front() {
        if (isEmpty() == true) {
            return -1;
        }
        return data[head];
    }


    public int Rear() {
        if (isEmpty() == true) {
            return -1;
        }
        return data[tail];
    }

    public static void main(String[] args) {

        MyCircularQueue myCircularQueue=new MyCircularQueue(5);
        System.out.println(myCircularQueue.enQueue(1));
//        // 1. Initialize a queue.
//        Queue<Integer> q = new LinkedList();
//        // 2. Get the first element - return null if queue is empty.
//        System.out.println("The first element is: " + q.peek());
//        // 3. Push new element.
//        q.offer(5);
//        q.offer(13);
//        q.offer(8);
//        q.offer(6);
//        // 4. Pop an element.
//        q.poll();
//        // 5. Get the first element.
//        System.out.println("The first element is: " + q.peek());
//        // 7. Get the size of the queue.
//        System.out.println("The size is: " + q.size());
    }
}
