package com.bewg.pd.test;

import java.util.ArrayList;
import java.util.List;

import com.bewg.pd.interf.util.*;
import org.testng.Reporter;
import org.testng.annotations.*;

import com.bewg.pd.interf.model.AutoLog;

public class TestRun {

    private static List<AutoLog> list = new ArrayList<AutoLog>();

    private String file = null;

    @Parameters({"filePath"})
    @BeforeTest
    public void beforetest(String a) {
        this.file = a;
    }

    @Test(dataProvider = "dp",priority=1)
    public void httpReq(Object Id, Object Test_is_exec, String TestCase, String Req_Type, String Req_host, String Req_interface, String Req_data, String Result_expected, String Is_Dep, String Dep_key,String Is_File) throws Exception {
        Reporter.log("用例编号值为：" + Id);
        Reporter.log("用例是否执行值为：" + Test_is_exec);
        Reporter.log("请求host:" + Req_host);
        Reporter.log("预期结果:" + Result_expected);

        String requrl = Req_host + Req_interface;
        String atresult = null;

        // System.out.println("原始数据"+Req_data);

        Req_data = PatternUtil.handlerReadataOfFun(Req_data);
        // System.out.println("函数表达式处理后"+Req_data);

        // 是否依赖上一条返回的数据,依赖处理
        Req_data = PatternUtil.handlerRedataofDep(Req_data);
        Reporter.log("请求数据：" + Req_data);
        System.out.println("依赖数据处理后" + Req_data);

        // System.out.println("if外的语句Test_is_exec的值为："+Test_is_exec);
        if ("YES".equals(Test_is_exec)) {
            System.out.println("id号为" + Id + "Test_is_exec的值为：" + Test_is_exec);
            // if(Test_is_exec.toString().length()>2){
            if ("GET".equals(Req_Type)) {
                // sendGet方法
                atresult = HttpReqUtil2.sendGet(requrl, Req_data, Req_Type);
            } else if ("DELETE".equals(Req_Type)) {
                // sendDelete方法
                atresult = HttpReqUtil2.sendDelete(requrl, Req_data, Req_Type);
            } else if ("PUT".equals(Req_Type)) {
                atresult = HttpReqUtil2.sendPut(requrl, Req_data, Req_Type);
            } else {
                // sendPost方法,判断是否需要发送文件
                if("YES".equals(Is_File)){
                    atresult = HttpReqUtil2.sendPostFile(requrl,Req_data);
                }else{
                    atresult = HttpReqUtil2.sendPost(requrl, Req_data, Req_Type);
                }

            }
        } else {
            System.out.println("id号为" + Id + "Test_is_exec的值为：" + Test_is_exec + "；所以不执行");
        }
        // reportng报告
        Reporter.log("实际结果值" + atresult);
        // 判断是否依赖
        if ("YES".equals(Is_Dep)) {
            //
            PatternUtil.isDepCase(Dep_key, atresult);
        }
        int result = PatternUtil.conpareResultTodb(atresult, Result_expected);
        list.add(new AutoLog(TestCase, Req_Type, requrl, Req_data, Result_expected, atresult, result, DateTimeUtil.getDateTime()));
        // 预期值与实际值对比
        PatternUtil.conpareResult(atresult, Result_expected);

    }

    @Test(dataProvider = "dp2",priority=2,enabled = false)
    public void httpReq2(Object Id, Object Test_is_exec, String TestCase, String Req_Type, String Req_host, String Req_interface, String Req_data, String Result_expected, String Is_Dep, String Dep_key) throws Exception {
        Reporter.log("用例编号值为：" + Id);
        Reporter.log("用例是否执行值为：" + Test_is_exec);
        Reporter.log("请求host:" + Req_host);
        Reporter.log("预期结果:" + Result_expected);

        String requrl = Req_host + Req_interface;
        String atresult = null;

        // System.out.println("原始数据"+Req_data);

        Req_data = PatternUtil.handlerReadataOfFun(Req_data);
        // System.out.println("函数表达式处理后"+Req_data);

        // 是否依赖上一条返回的数据,依赖处理
        //Req_data = PatternUtil.handlerRedataofDep(Req_data);
        Reporter.log("请求数据：" + Req_data);
        System.out.println("依赖数据处理后" + Req_data);

        // System.out.println("if外的语句Test_is_exec的值为："+Test_is_exec);
        if ("YES".equals(Test_is_exec)) {
            System.out.println("id号为" + Id + "Test_is_exec的值为：" + Test_is_exec);
            // if(Test_is_exec.toString().length()>2){
            if ("GET".equals(Req_Type)) {
                // sendGet方法
                atresult = HttpReqUtil2.sendGet(requrl, Req_data, Req_Type);
            } else if ("DELETE".equals(Req_Type)) {
                // sendDelete方法
                atresult = HttpReqUtil2.sendDelete(requrl, Req_data, Req_Type);
            } else if ("PUT".equals(Req_Type)) {
                atresult = HttpReqUtil2.sendPut(requrl, Req_data, Req_Type);
            } else {
                // sendPost方法
                atresult = HttpReqUtil2.sendPost(requrl, Req_data, Req_Type);
            }
        } else {
            System.out.println("id号为" + Id + "Test_is_exec的值为：" + Test_is_exec + "；所以不执行");
        }
        // reportng报告
        Reporter.log("实际结果值" + atresult);
        // 判断是否依赖
        if ("YES".equals(Is_Dep)) {
            //
            PatternUtil.isDepCase(Dep_key, atresult);
        }
        int result = PatternUtil.conpareResultTodb(atresult, Result_expected);
        list.add(new AutoLog(TestCase, Req_Type, requrl, Req_data, Result_expected, atresult, result, DateTimeUtil.getDateTime()));
        // 预期值与实际值对比
        PatternUtil.conpareResult(atresult, Result_expected);

    }

    @DataProvider
    public Object[][] dp() throws Exception {
        ExcelUtil excel = new ExcelUtil(file);
        return excel.fromCellTypeGetCellValue(0);
    }

    @DataProvider
    public Object[][] dp2() throws Exception {
        ExcelUtil excel = new ExcelUtil(file);
        return excel.fromCellTypeGetCellValue(0);
    }

    @AfterTest
    public void afterTest() throws Exception {
        System.out.println("运行结束！");

    }

}
