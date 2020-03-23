package com.shanshuan.stack;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by wangzifeng on 2020/3/19.
 */
public class Solution {
    // Hash table that takes care of the mappings.
    private HashMap<Character, Character> mappings;

    // Initialize hash map with mappings. This simply makes the code easier to read.
    public Solution() {
        this.mappings = new HashMap<Character, Character>();
        this.mappings.put(')', '(');
        this.mappings.put('}', '{');
        this.mappings.put(']', '[');
    }

    public boolean isValid(String s) {

        // Initialize a stack to be used in the algorithm.
        Stack<Character> stack = new Stack<Character>();
            if((s.length()&1)==1){
                return false;
            }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(mappings.containsKey(c)){
                char topElement = stack.empty() ? '#' : stack.pop();
                if(mappings.get(c)!=topElement){
                    return false;
                }
            }else{
                stack.push(c);
            }

        }

        return stack.isEmpty();
    }
}
