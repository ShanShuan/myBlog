package com.shanshuan.stack;



/**
 * Created by wangzifeng on 2020/3/20.
 * 根据每日 气温 列表，请重新生成一个列表，对应位置的输出是需要再等待多久温度才会升高超过该日的天数。如果之后都不会升高，请在该位置用 0 来代替。
 *
 * 例如，给定一个列表 temperatures = [73, 74, 75, 71, 69, 72, 76, 73]，你的输出应该是 [1, 1, 4, 2, 1, 1, 0, 0]。
 *
 * 提示：气温 列表长度的范围是 [1, 30000]。每个气温的值的均为华氏度，都是在 [30, 100] 范围内的整数。
 */
public class MySolution {


//    /**
//     * 暴力   没有算法
//     * @param T
//     * @return
//     */
//    public int[] dailyTemperatures(int[] T) {
//
//        int[] res=new int[T.length];
//
//        for (int i = 0; i < T.length; i++) {
//            int courrent = T[i];
//            for (int j = i+1; j <T.length ; j++) {
//                if(courrent<T[j]){
//                    res[i]=j-i;
//                    break;
//                }
//            }
//        }
//        return res;
//    }


    /**
     * 动态规划
     * 从最右边开始 计算，利用  右边的计算结果
     * @param T
     * @return
     */
    public int[] dailyTemperatures(int[] T) {

        int[] res=new int[T.length];
        res[T.length-1]=0;
        for (int i = T.length-2; i >=0 ; i--) {
            for (int j = i+1; j <T.length; j=res[j]+j) {
                if(T[i]<T[j]){
                    res[i]=j-i;
                    break;
                }else if(res[j]==0){
                    res[i]=0;
                    break;
                }
            }
        }
        return res;
    }
}
