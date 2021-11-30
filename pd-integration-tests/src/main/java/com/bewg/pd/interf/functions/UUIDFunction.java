package com.bewg.pd.interf.functions;

import java.util.UUID;

/**
 * 
 *
 * @Title: UUIDFunction.java
 * @Description: 生成一个uuid字符串
 * @author: maxiaokui
 * @date: 2021/10/26
 * @version V1.0
 *
 * 
 */

public class UUIDFunction implements Function {

    @Override
    public String getFunc() {

        return "UUID";
    }

    @Override
    public String execparm(String[] args) {

        String result = null;

        result = UUID.randomUUID().toString().replace("-", "");

        return result;
    }

}
