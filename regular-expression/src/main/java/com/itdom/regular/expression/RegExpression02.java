package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpression02 {
    public static void main(String[] args) {
        String content = "abc(abc(123(";
        //在Java语言中中使用\\代表其他语言的\
//        String regStr = "\\(";
        //标识匹配三个数字
//        String regStr = "\\d\\d\\d";
        //等效于
        String regStr = "\\d{3}";
                Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println("找到:"+matcher.group(0));
        }

    }
}
