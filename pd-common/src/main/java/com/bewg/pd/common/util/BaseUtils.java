package com.bewg.pd.common.util;

public class BaseUtils {

    /**
     * 将字符串转为Double
     *
     * @param oriStr
     * @return
     */
    public static Double dealObjectWithDouble(Object oriStr) {
        Double nowValue = 0.0;
        try {
            if (oriStr != null) {
                nowValue = Double.valueOf(oriStr.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return nowValue;
    }

    /**
     * 将object转换为string
     * 
     * @param object
     * @return
     */
    public static String dealObjectWithString(Object object) {
        String result = "";
        if (object != null) {
            result = object.toString();
        }
        return result;
    }

    /**
     * 正数/负数/小数
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        String reg = "-?[0-9]+.?[0-9]{0,}";
        return str.matches(reg);
    }
}
