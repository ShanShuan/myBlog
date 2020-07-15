package com.shanshuan.questions;

import java.util.List;
import java.util.Vector;

/**
 * @Description :
 * @Author : wangzifeng
 * @Createon : 2020/7/14.
 */
public class Day1 {


    /**
     * 三角形 返回最小路径 和
     * @param triangle
     * @return
     */
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] f = new int[n][n];
        f[0][0] = triangle.get(0).get(0);
        for (int i = 1; i < n; ++i) {
            f[i][0] = f[i - 1][0] + triangle.get(i).get(0);
            for (int j = 1; j < i; ++j) {
                f[i][j] = Math.min(f[i - 1][j - 1], f[i - 1][j]) + triangle.get(i).get(j);
            }
            f[i][i] = f[i - 1][i - 1] + triangle.get(i).get(i);
        }
        int minTotal = f[n - 1][0];
        for (int i = 1; i < n; ++i) {
            minTotal = Math.min(minTotal, f[n - 1][i]);
        }
        return minTotal;
    }

    /**
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for (int i = 0; i < nums.length-1; i++) {
            int de = target - nums[i];
            for (int j = i+1; j < nums.length; j++) {
                if(nums[j]==de){
                    result[0]=i;
                    result[1]=j;
                    return result;
                }
            }
        }
        return null;
    }

}
