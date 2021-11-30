package com.bewg.pd.interf.functions;

import com.bewg.pd.interf.util.DateTimeUtil;

/**
 * 
 *
 * @Title: TimeFunction.java
 * @Description: 调用time类
 * @author: maxiaokui
 * @date: 2021/10/26
 * @version V1.0
 *
 * 
 */

public class TimeFunction implements Function {

    @Override
    public String getFunc() {

        return "Time";
    }

    @Override
    public String execparm(String[] args) {
        String result = null;
        if (args.length == 0) {
            // 时间戳
            result = String.valueOf(DateTimeUtil.getTimeTmp1());
        } else if (args.length == 1 && args[0].equals("YMDHMS")) {
            // 年月日时分秒
            result = DateTimeUtil.getDateTime();
        } else if (args.length == 1 && args[0].equals("YMD")) {
            // 年月日
            result = DateTimeUtil.getDate();
        } else if (args.length == 1 && args[0].equals("HMS")) {
            // 时分秒
            result = DateTimeUtil.getTime();
        } else {
            // 定制格式
            result = DateTimeUtil.getPatternDateTime(args[0]);
        }
        return result;

    }

}
