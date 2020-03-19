package com.shanshuan.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 与队列不同，栈是一个 LIFO 数据结构。通常，插入操作在栈中被称作入栈 push 。
 * 与队列类似，总是在堆栈的末尾添加一个新元素。但是，删除操作，退栈 pop ，将始终删除队列中相对于它的最后一个元素
 * Created by wangzifeng on 2020/3/16.
 */
public class MyStack {
    private List<Integer> data;

    public MyStack() {
        data=new ArrayList<Integer>();
    }
    public int pop(){
        if(ifEmpty()){
            return -1;
        }
        Integer integer = data.get(data.size() - 1);
        data.remove(data.size() - 1);
        return integer;
    }


    public int peek(){
        if(ifEmpty()){
            return -1;
        }
        return data.get(data.size() - 1);
    }

    private boolean ifEmpty() {
        return data.isEmpty();
    }

    public boolean push(int a){
        data.add(a);
        return true;
    }


    public static void main(String[] args) {
        // 1. Initialize a stack.
        Stack<Integer> s = new Stack<>();
        // 2. Push new element.
        s.push(5);
        s.push(13);
        s.push(8);
        s.push(6);
        // 3. Check if stack is empty.
        if (s.empty() == true) {
            System.out.println("Stack is empty!");
            return;
        }
        // 4. Pop an element.
        s.pop();
        // 5. Get the top element.
        System.out.println("The top element is: " + s.peek());
        // 6. Get the size of the stack.
        System.out.println("The size is: " + s.size());
    }
}
