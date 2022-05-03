package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpression05 {
    public static void main(String[] args) {
        String content = "11111aaaa11111";

//        String regStr = "a{3}";
//        String regStr = "1{3}";
        //两位的任意得数字
//        String regStr = "\\d{2}";

        //表示至少3个a，最多4个a
//        String regStr = "a{3,4}";
        //匹配2位或者3，4，5位数字组成的数字
//        String regStr = "\\d{2,5}";
        //匹配至少一个1
        String regStr = "1+";


                Pattern pattern = Pattern.compile(regStr,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println("找到:"+matcher.group(0));
        }

    }
}
