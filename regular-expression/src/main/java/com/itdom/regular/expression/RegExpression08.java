package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpression08 {
    public static void main(String[] args) {
        String content = "zhangsan1992 9928 5689";
        //非命名捕获
//        String regEXP = "(\\d\\d)(\\d\\d)";
        //命名捕获
        String regEXP = "(?<g1>\\d\\d)(?<g2>\\d\\d)";

        Pattern pattern = Pattern.compile(regEXP);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.print("找到:" + matcher.group(0) + "\t");
            System.out.print("1组:" + matcher.group(1) + "\t");
            System.out.print("2组:" + matcher.group(2) + "\t");
            System.out.println("命名分组:" + matcher.group("g1") + "\t" + matcher.group("g2"));
        }
    }
}
