package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分组，捕获，反向引用
 */
public class RegExpression12 {
    public static void main(String[] args) {
        //反向引用
        String content = "1221 3553 6578";
        //group(0)表示匹配的字符串，1表示的是第一组，2表示的第二组
        String regEXP = "(\\d)(\\d)\\2\\1";
//        String regEXP = "\\d{4}";

        Pattern pattern = Pattern.compile(regEXP);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.println("找到:" + matcher.group(0));

        }
    }
}
