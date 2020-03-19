package com.shanshuan.stack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzifeng on 2020/3/16.
 */
class MinStack {
      private List<Integer> data;

        /** initialize your data structure here. */
        public MinStack() {
            data=new ArrayList<>();
        }

        public void push(int x) {
            data.add(x);
        }

    private boolean isEmpty() {
       return data.isEmpty();
    }

    public void pop() {
                if(isEmpty()){
                    return;
                }
                 data.remove(data.size()-1);
        }

        public int top() {
            if(isEmpty()){
                return 0;
            }
            return  data.get(data.size()-1);
        }

        public int getMin() {
            int min = 0;
            for (int i = 0; i < data.size(); i++) {
                if(i==0) {min=data.get(i);}else {
                    if (min > data.get(i)) min = data.get(i);
                }
            }
            return min;
        }

    public static void main(String[] args) {
        MinStack obj = new MinStack();
        obj.push(2);
        obj.push(0);
        obj.push(3);
        obj.push(0);
        System.out.println(obj.getMin());
        obj.pop();
        System.out.println(obj.getMin());
        obj.pop();
        System.out.println(obj.getMin());
        obj.pop();
        System.out.println(obj.getMin());
        obj.pop();
    }
    }


