package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮箱匹配
 */
public class RegExpression14 {
    public static void main(String[] args) {
        String content = "772571632@chend.@qq.com";
        String regEXP = "^[\\w-]+@[\\w-.]+[\\w-]+$";

        if (content.matches(regEXP)){
            System.out.println("匹配成功！！");
        }else {
            System.out.println("匹配失败");
        }

    }
}
