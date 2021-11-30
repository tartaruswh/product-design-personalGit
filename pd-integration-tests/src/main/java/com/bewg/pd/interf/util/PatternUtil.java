package com.bewg.pd.interf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONPath;

import junit.framework.Assert;

public class PatternUtil {
    // 定义一个正则表达式
    public static String conpareResultreg = "(\\$\\.[\\w-+]+):([\\u4e00-\\u9fa5\\w]+)";
    // 是否依赖和被依赖的正则表达式
    // public static String isDepreg ="([\\w/]+):([\\$\\.\\w]+)";
    public static String isDepreg = "([\\w/]+):([\\$\\.\\w\\[\\]]+)";
    // 请求数据中的函数表达式 ${__Random(1,10,100)}
    public static String functionRegex = "\\$\\{\\_\\_(\\w+)(\\([\\w,]+\\))\\}";
    private static Map<String, String> map = new HashMap<String, String>();

    /**
     *
     * @Title: getMatcher @Description: 一个正则表达式方法 @param: @param reg @param: @param data @param: @return @return: Matcher @throws
     */
    public static Matcher getMatcher(String reg, String data) {
        // 传入一个正则表达式
        Pattern pa = Pattern.compile(reg);
        // 传入一个数据
        return pa.matcher(data);
    }

    /**
     *
     * @Title: handlerReadataOfFun @Description: 请求数据中的函数表达式进行分析转换等；比如时间戳，md5 @param: @param parevalue @param: @param reqdata @param: @return @param: @throws Exception @return: String @throws
     */

    public static String handlerReadataOfFun(String reqdata) throws Exception {

        Matcher matcher = getMatcher(functionRegex, reqdata);
        while (matcher.find()) {
            String groupName = matcher.group(); // ${__Random(1,10,100)}
            String funName = matcher.group(1);// Random
            String[] funparam = matcher.group(2).replace("(", "").replace(")", "").split(",");// [1,10,100]

            String value = null;

            if (FuncMapingClassUtil.isFunc(funName)) {
                value = FuncMapingClassUtil.getvalue(funName, funparam);
            }
            reqdata = StringUtil.replace(reqdata, groupName, value);

        }
        return reqdata;
    }

    /**
     * 替换需要依赖的值
     */
    public static String handlerRedataofDep(String reqData) {

        Matcher matcher = getMatcher(isDepreg, reqData);

        while (matcher.find()) {
            String value = map.get(matcher.group());
            // 替换值开始
            reqData = StringUtil.replace(reqData, matcher.group(), value);
        }
        return reqData;
    }

    /**
     *
     * @Title: isDepCase @Description: 是否被依赖，如果被依赖值则把返回的值通过正则表达式存放在hashmap里 @param: @param Dep_key @param: @param atresult @return: void @throws
     */
    public static void isDepCase(String Dep_key, String atresult) {

        String value = null;
        // 调用getmacher
        Matcher matcher = getMatcher(isDepreg, Dep_key);

        while (matcher.find()) {
            //System.out.println("这是组"+matcher.group());
            //System.out.println("这是组1"+matcher.group(1));
            //System.out.println("这是组2"+matcher.group(2));
            //获取产品列表数组的长度
            int i = JSONPath.size(atresult, "$.result.childProductMemberList") - 1;
            //判断依赖值
            if ("/v1/baseinfo/productMember/productMemberTree:$.result.childProductMemberList[]".equals(Dep_key)) {
                value = (i + 2) + "";
            } else if("/v1/baseinfo/productMember/productMemberTree:$.result.childProductMemberList[1].productMemberVo.id".equals(Dep_key)){
                String key = "$.result.childProductMemberList[" + i + "].productMemberVo.id";
                System.out.println("最新的json串==========" + key);
                // JSONPath.read(atresult, "$.result.childProductMemberList[i].productMemberVo.id").toString();
                value = JSONPath.read(atresult, key).toString();
                System.out.println("查看id值========" + value);
            }else {
                //用组2获取json表达式
                value = JSONPath.read(atresult, matcher.group(2)).toString();
            }
            // 放到map里因为map格式是key:value
            map.put(matcher.group(), value);
        }
    }

    /**
     * 预期值与实际结果值进行对比
     *
     * @param atresult
     * @param Result_expected
     */
    public static void conpareResult(String atresult, String Result_expected) {

        // 预期结果返回值

        Matcher matcher = getMatcher(conpareResultreg, Result_expected);
        System.out.println("预期结果值与实际结果值开始对比" + matcher.find());
        while (matcher.find()) {
            String expjsonPath = matcher.group(1);
            System.out.println("预期结果里的组一：" + expjsonPath);
            String expvalue = matcher.group(2);
            System.out.println("预期结果里的组二：" + expvalue);
            // 实际结果返回值
            String actvalue = JSONPath.read(atresult, expjsonPath).toString();
            // assert 发现有失败的用例后续的用例都不再执行
            Assert.assertEquals(expvalue, actvalue);
        }
    }

    // 存储数据库？
    public static int conpareResultTodb(String atresult, String Result_expected) {

        int flag = 0;
        List<Integer> list = new ArrayList<Integer>();

        // 预期结果返回值
        // System.out.println("预期结果返回值"+Result_expected);
        Matcher matcher = getMatcher(conpareResultreg, Result_expected);
        while (matcher.find()) {
            String expjsonPath = matcher.group(1);
            String expvalue = matcher.group(2);
            // 实际结果返回值
            String actvalue = JSONPath.read(atresult, expjsonPath).toString();
            int staus = actvalue.equals(expvalue) ? 1 : 0;
            list.add(staus);
        }
        if (!list.contains(0)) {
            flag = 1;
        }
        return flag;
    }

    public static void main() {}
}
