package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpression03 {
    public static void main(String[] args) {
        String content = "a11c_8abc ABC#$@.\n";

//        String regStr = "[a-z]";
//        String regStr = "[A-Z]";
//        String regStr = "[A-Z]";
        //严格的匹配字符串abc,区分大小写,也可以使用Pattern pattern = Pattern.compile(regStr,Pattern.CASE_INSENSITIVE);
//        String regStr = "abc";
        //如果不想区分大小写，可以使用(?!)abc
//        String regStr = "(?i)abc";
        //匹配的0-9的任意一个字符
//        String regStr = "[0-9]";
        //匹配的不在a-z中的任意两个连续的字符
//        String regStr = "[^a-z]{2}";
        //匹配不在0-9的任意一个字符
//        String regStr = "[^0-9]";
//        String regStr = "[abcd]";
        //匹配到数字，字母，下划线<<===>>[a-zA-Z0-9_]
//        String regStr = "\\w";
        //匹配所有非数字，字母，下划线的字符<<====>>[^a-zA-Z0-9_]
//        String regStr = "\\W";
        //匹配任意空格
//        String regStr = "\\s";
        //匹配任意非空格
//        String regStr = "\\S";
        String regStr = "\\.";


                Pattern pattern = Pattern.compile(regStr,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println("找到:"+matcher.group(0));
        }

    }
}
