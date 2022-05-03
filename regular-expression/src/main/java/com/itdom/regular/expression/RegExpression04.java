package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpression04 {
    public static void main(String[] args) {
        String content = "chendom陈东陈冬";

        String regStr = "chen|陈|东|冬";


                Pattern pattern = Pattern.compile(regStr,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println("找到:"+matcher.group(0));
        }

    }
}
