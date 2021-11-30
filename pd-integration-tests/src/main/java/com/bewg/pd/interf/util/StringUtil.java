package com.bewg.pd.interf.util;

public class StringUtil {

    public static String replace(String Req_data, String matcher, String value) {
        // 查看要传入的字符串的长度
        int matcherlenth = matcher.length();
        // 查看所在的位置
        int strint = Req_data.indexOf(matcher);
        // 输出左侧字符串的信息
        String strleft = Req_data.substring(0, strint);
        // 输出右侧字符串信息
        int right = strint + matcherlenth;
        String strright = Req_data.substring(right);
        // 把左侧的值和新值、右侧的值拼接起来，并返回；
        return strleft + value + strright;

    }

    public static void main(String[] args) {

    }

}
