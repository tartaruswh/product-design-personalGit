package com.bewg.pd.interf.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * 
 *
 * @Title: RetryAnalyzerImpl.java
 * @Description: 用例失败为是（true）,调用retry方法，最多再执行3次，可配置次数
 * @author: maxiaokui
 * @date: 2021/10/26
 * @version V1.0
 *
 * 
 */
public class RetryAnalyzerImpl implements IRetryAnalyzer {

    private int retryCount = 1;
    private int retryMaxCount = 4;

    @Override
    // 用例是否执行失败，是(true)则执行下面的方法，否(false)则不执行
    public boolean retry(ITestResult result) {
        if (retryCount < retryMaxCount) {
            retryCount++;
            return true;
        }
        retryCount = 1;
        return false;

    }

}
