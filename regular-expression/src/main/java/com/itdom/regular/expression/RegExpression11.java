package com.itdom.regular.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1. 汉字
 * 2. 邮政编码
 * 3. QQ号
 *    要求:是1-9开头的一个(5位数-10位数)，比如123890,1345678,1807698765
 * 4. 手机号码
 *  要求:必须以13，14，15，18开头的11位数，比如13588889999
 * 5. URL:
 *
 */
public class RegExpression11 {
    public static void main(String[] args) {
        //1. 汉字
//        String content = "张1三李四学习";
//        String regEXP = "^[\u0391-\uffe5]+$";
        //邮编
//        String content = "536000";
//        String regEXP = "^[1-9]\\d{5}";
        //QQ号码
//        String content = "772571631";
//        String regEXP = "^[1-9]\\d{4,9}";
        //手机号
//        String content = "18039306831";
//        String regEXP = "^1[3|4|3|8]\\d{9}";
        String content = "https://www.bilibili.com/video/BV1Eq4y1E79W?p=17&spm_id_from=pageDriver";
//        String regEXP = "^((https|http)://)?([\\w-]+\\.)+[\\w-]+(\\/[\\w-=?%/#$&*.]*)?$";
        String regEXP = "^((http|https)://)?([\\w-]+\\.)+[\\w-]+(\\/[\\w-=?%/#$&*.]*)?$";

        Pattern pattern = Pattern.compile(regEXP);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.println("找到:" + matcher.group(0));

        }
    }
}
