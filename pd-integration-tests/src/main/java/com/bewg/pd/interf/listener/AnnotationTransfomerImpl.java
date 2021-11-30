package com.bewg.pd.interf.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

/**
 *
 *
 * @Title: AnnotationTransfomerImpl.java
 * @Description: 用例失败执行重试，调用的RetryAnalyzerImll类
 * @author: maxiaokui
 * @date: 2021/10/26
 * @version V1.0
 *
 *
 */

public class AnnotationTransfomerImpl implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

        // 这个类必须被实现，实现后才能进行下面的用例重试，
        IRetryAnalyzer retry = annotation.getRetryAnalyzer();

        annotation.setRetryAnalyzer(RetryAnalyzerImpl.class);

    }

}
