package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpression07 {
    public static void main(String[] args) {
        String content = "chenpro prochen pluschen";
        //以至少一个数字开头，任意个字母结尾
//        String regEXP = "^[0-9]+[a-z]*";

        //含义是以至少1个数字开头，至少一个字母结尾的字符串
//            String regEXP = "^[0-9a-zA-Z]+[a-z]+$";
        //表示匹配目标字符串的边界，这里说的字符串的边界指的是子串间由空格，或者是目标字符串的结束位置
//        String regEXP = "chen\\b";
        //和\\b恰恰相反
        String regEXP = "chen\\B";

        Pattern pattern = Pattern.compile(regEXP);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println("找到:"+matcher.group(0));
        }

    }
}
