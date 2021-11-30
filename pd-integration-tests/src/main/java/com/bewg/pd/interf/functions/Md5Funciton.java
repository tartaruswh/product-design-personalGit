package com.bewg.pd.interf.functions;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 *
 * @Title: Md5Funciton.java
 * @Description: 生成一個md5
 * @author: maxiaokui
 * @date: 2021/10/26
 * @version V1.0
 *
 * 
 */

public class Md5Funciton implements Function {

    @Override
    public String getFunc() {

        return "Md5";
    }

    @Override
    public String execparm(String[] args) {

        String result = null;

        if (args.length == 1) {
            result = DigestUtils.md5Hex(args[0]);
        }

        return result;
    }

}
