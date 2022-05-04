package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpression09 {
    public static void main(String[] args) {
        String content = "阳关灿烂 阳关男孩 阳关帅气";
        //非命名捕获
//        String regEXP = "(\\d\\d)(\\d\\d)";
        //命名捕获
//        String regEXP = "(?<g1>\\d\\d)(?<g2>\\d\\d)";
//        String regEXP = "(?<g1>\\d\\d)(?<g2>\\d\\d)";
        String regEXP = "阳关(?:灿烂|帅气)";
//        String regEXP = "阳关(?=灿烂|帅气)";
//        String regEXP = "阳关(?!灿烂|帅气)";

        Pattern pattern = Pattern.compile(regEXP);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.println("找到:" + matcher.group(0));

        }
    }
}
