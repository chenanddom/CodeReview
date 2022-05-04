package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpression10 {
    public static void main(String[] args) {
        String content = "abcfff";
        //匹配多个的贪婪匹配
//        String regEXP = "f+";
        //实现非贪婪匹配,匹配到单个就返回
        String regEXP = "f+?";

        Pattern pattern = Pattern.compile(regEXP);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.println("找到:" + matcher.group(0));

        }
    }
}
