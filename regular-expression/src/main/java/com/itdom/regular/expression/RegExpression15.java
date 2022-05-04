package com.itdom.regular.expression;

/**
 * URL匹配
 */
public class RegExpression15 {
    public static void main(String[] args) {
        String content = "https://seata.io:443/zh-cn/docs/overview/what-is-seata.html";
        String regEXP = "^(http|https)://([a-z-A-Z.]+)(:\\d+)*[\\w-/]*([\\w.])+$";

        if (content.matches(regEXP)){
            System.out.println("匹配成功！！");
        }else {
            System.out.println("匹配失败");
        }

    }
}
