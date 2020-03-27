package com.shanshuan.QuickSort;

/**
 * Created by wangzifeng on 2020/3/24.
 * 快排
 */
public class QuickSort {

    public void QuickSort(int[] num,int left,int right){
        int t;
        /*只有一个元素*/
        if(left>=right){
            return;
        }
        int i=left;
        int j=right;
        int base=num[left];
        while(i<j){
            while(num[j]>=base&&i<j){
                j--;
            }
            while(num[i]<=base&&i<j){
                i--;
            }
            if(i<j){
                t = num[j];
                num[j] = num[i];
                num[i] = t;
            }
        }

        //递归调用左半数组
        QuickSort(num, left, i-1);
        //递归调用右半数组
        QuickSort(num, i+1, right);
    }
}
