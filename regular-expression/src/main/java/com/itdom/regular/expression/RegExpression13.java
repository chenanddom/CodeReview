package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理结巴程序
 */
public class RegExpression13 {
    public static void main(String[] args) {
        String content = "我...我...要要...学学Java";
        String regEXP = "\\.";

        Pattern pattern = Pattern.compile(regEXP);
        content = pattern.matcher(content).replaceAll("");
        System.out.println(content);

        content = Pattern.compile("(.)\\1").matcher(content).replaceAll("$1");
        System.out.println(content);

    }
}
