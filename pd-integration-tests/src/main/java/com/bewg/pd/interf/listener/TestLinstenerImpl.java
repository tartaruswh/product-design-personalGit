package com.bewg.pd.interf.listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestLinstenerImpl implements ITestListener {

    ArrayList<ITestResult> testBeRemove = new ArrayList<ITestResult>();

    @Override
    public void onFinish(ITestContext context) {

        // 成功用例结合
        Set<ITestResult> allPassTests = context.getPassedTests().getAllResults();
        for (ITestResult allpasstests : allPassTests) {
            System.out.println("com.bewg.interf.listener包下的TestLinstenerImpl 输出 成功用例集合：\n" + allpasstests);
        }
        // 失败用例set集合，因为set不能有重复数据
        Set<Integer> failedTestIds = new HashSet<Integer>();
        // 失败用例结合
        Set<ITestResult> allfaileTests = context.getFailedTests().getAllResults();
        for (ITestResult allfailetests : allfaileTests) {
            int failedHashcodeId = allfailetests.toString().hashCode();// id代表失败用例集合
            if (failedTestIds.contains(failedHashcodeId)) {
                // testBeRemove.add(allfailetests);
                // 删除重复的失败用例
                allfaileTests.remove(allfailetests);
            } else {
                failedTestIds.add(failedHashcodeId);
                System.out.println("com.bewg.interf.listener包下的TestLinstenerImpl 输出失败用例集合" + allfailetests);
            }
        }
        for (ITestResult case1 : testBeRemove) {
            System.out.println("com.bewg.interf.listener包下的TestLinstenerImpl 输出 重复用例集合=" + case1);
            // allfaileTests.remove(case1);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        // System.out.println("我是在test方法执行前运行：onTestStart");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // System.out.println("我是在test方法执行成功后运行：onTestSuccess");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // System.out.println("我是在test方法执行失败后运行：onTestFailure");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // System.out.println("我是在test方法执行跳过后运行：onTestSkipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // System.out.println("我是在test方法执行失败后切在成功百分比范围内运行：onTestFailedButWithinSuccessPercentage");
    }

    @Override
    public void onStart(ITestContext context) {
        // System.out.println("我是在实例化前运行：onStart");
    }

}
