package com.itdom.linear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReversePolishNotation {
    public static void main(String[] args) {
//中缀表达式3*（17-15）+18/6的逆波兰表达式如下
        String[] notation = {"3", "17", "15", "-", "*", "18", "6", "/", "+"};
        System.out.println(caculate(notation));
    }

    public static int caculate(String[] notation){
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < notation.length; i++) {

            String current = notation[i];
            Integer v1;
            Integer v2;
            Integer result;
            switch (current){
                case "+":
                    v1=stack.pop();
                    v2=stack.pop();
                    result = v2+v1;
                    stack.push(result);
                    break;
                case "-":
                    v1=stack.pop();
                    v2=stack.pop();
                    result = v2-v1;
                    stack.push(result);
                    break;
                case "*":
                    v1=stack.pop();
                    v2=stack.pop();
                    result = v2*v1;
                    stack.push(result);
                    break;
                case "/":
                    v1=stack.pop();
                    v2=stack.pop();
                    result = v2/v1;
                    stack.push(result);
                    break;
                default:
                    stack.push(Integer.valueOf(notation[i]));
                    break;
            }
        }
        //遍历完之后最后留在栈内的就是结果
        Integer ret = stack.pop();
        return ret;
    }
}
