package com.bewg.pd.interf.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

/**
 *
 *
 * @Title: HttpReqUtil.java
 * @Description: 这里是get和post请求方法
 * @author: maxiaokui
 * @date: 2021年4月13日 下午10:20:48
 * @version V1.0
 *
 *
 */
public class HttpReqUtil2 {
    // 每次的请求cookie是共用的,整个系统共享一个cookie
    private static CookieStore cookieStore = new BasicCookieStore();

    // 请求头设置信息和设置请求超时对应的操作方法
    public static void httpRequestConfig(HttpRequestBase httpRequestBase, String parm) {

        httpRequestBase.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:87.0) Gecko/20100101 Firefox/87.0");

        if (JsonUtil.isJsonArray(parm) || JsonUtil.isJsonString(parm)) {
            httpRequestBase.setHeader("Content-Type", "application/Json; charset=UTF-8");
        } else {
            httpRequestBase.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        }

        // 设置请求超时对应的操作方法

        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000).build();
        httpRequestBase.setConfig(requestConfig);

    }

    /**
     *
     * @Title: sendGet @Description: 发送get请求和得到String类型的返回值 @param: @param url @param: @param parm @param: @return @param: @throws IOException @return: String @throws
     */
    public static String sendGet(String url, String parm, String Req_Type) throws IOException {
        String result = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse reponse = null;
        //String finaurl = url + "?" + parm;  去掉？号写在用例里
        String finaurl = url + parm;
        try {
            // A 得到httpclient对象和cookie存储区，用来发送请求
            // httpclient =HttpClients.createDefault();
            httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

            // B 发送请求
            HttpGet httpget = new HttpGet(finaurl);
            // 设置请求头信息和设置请求超时对应的操作方法
            httpRequestConfig(httpget, parm);

            // 会得到服务器返回值
            reponse = httpclient.execute(httpget);
            // 得到服务器返回码
            int statuscode = reponse.getStatusLine().getStatusCode();
            // 判断返回码是否为200
            if (statuscode == HttpStatus.SC_OK) {
                // C 研究服务器返回值，但返回的不是字符串类型
                HttpEntity entity = reponse.getEntity();

                // E 研究EntityUtils，转换成字符串类型
                // result = EntityUtils.toString(entity, "utf-8");
                result = JSON.parseObject(EntityUtils.toString(entity, "utf-8")).toString();
                System.out.println("此处是" + Req_Type + "里的返回值,方法是sendget" + result);
            } else {
                result = "您的服务器返回码为" + statuscode + "请检查您的地址";
            }
            List<Cookie> listCookie = cookieStore.getCookies();
            for (Cookie cookie : listCookie) {
                System.out.println(cookie.getName() + cookie.getValue());

            }
        } finally {
            reponse.close();
            httpclient.close();
        }
        return result;
    }

    /**
     * 删除请求方法
     * 
     * @author maxiaokui
     * @date 2021/10/26 16:07
     * @param
     * @param
     * @return java.lang.String
     */

    public static String sendDelete(String url, String parm, String Req_Type) throws IOException {
        String result = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse reponse = null;
        String finaurl = url + parm;
        try {
            // A 得到httpclient对象和cookie存储区，用来发送请求
            // httpclient =HttpClients.createDefault();
            httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

            // B 发送请求
            // HttpGet httpget = new HttpGet(finaurl);
            HttpDelete httpdelete = new HttpDelete(finaurl);
            // 设置请求头信息和设置请求超时对应的操作方法
            httpRequestConfig(httpdelete, parm);

            // 会得到服务器返回值
            reponse = httpclient.execute(httpdelete);
            // 得到服务器返回码
            int statuscode = reponse.getStatusLine().getStatusCode();
            // 判断返回码是否为200
            if (statuscode == HttpStatus.SC_OK) {
                // C 研究服务器返回值，但返回的不是字符串类型
                HttpEntity entity = reponse.getEntity();

                // E 研究EntityUtils，转换成字符串类型
                // result = EntityUtils.toString(entity, "utf-8");
                result = JSON.parseObject(EntityUtils.toString(entity, "utf-8")).toString();
                System.out.println("此处是" + Req_Type + "里的返回值,方法是senddelete" + result);
            } else {
                result = "您的服务器返回码为" + statuscode + "请检查您的地址";
            }
            List<Cookie> listCookie = cookieStore.getCookies();
            for (Cookie cookie : listCookie) {
                System.out.println(cookie.getName() + cookie.getValue());

            }
        } finally {
            reponse.close();
            httpclient.close();
        }
        return result;
    }

    /**
     *
     * @Title: sendPost @Description: 发送post请求 @param: @param url @param: @param parm @param: @return @param: @throws IOException @return: String @throws
     */
    public static String sendPost(String url, String parm, String Req_Type) throws IOException {
        String result = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse reponse = null;

        try {
            // A 得到httpclient对象和cookie存储区，用来发送请求
            // httpclient =HttpClients.createDefault();
            httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            // B 发送请求
            HttpPost httpPost = new HttpPost(url);
            // 设置请求头和设置请求超时
            httpRequestConfig(httpPost, parm);
            System.out.println("sendpost方法中的请求数据为：" + parm);
            // 请求时绑定数据
            httpPost.setEntity(new StringEntity(parm, "utf-8"));

            // 会得到服务器返回值
            reponse = httpclient.execute(httpPost);
            // 得到服务器返回码
            int statuscode = reponse.getStatusLine().getStatusCode();

            // 判断返回码是否为200
            if (statuscode == HttpStatus.SC_OK) {
                // C 研究服务器返回值，但返回的不是字符串类型
                HttpEntity entity = reponse.getEntity();
                // System.out.println("我是post里的返回码"+statuscode);
                // E 研究EntityUtils，转换成字符串类型
                // result = EntityUtils.toString(entity, "utf-8");
                result = JSON.parseObject(EntityUtils.toString(entity, "utf-8")).toString();
                System.out.println("此处是" + Req_Type + "里的返回值，方法是sendpost" + result);
            } else {
                result = "您的服务器返回码为" + statuscode + "请检查您的地址";
            }
            List<Cookie> listCookie = cookieStore.getCookies();
            for (Cookie cookie : listCookie) {
                System.out.println("cookie中的格式为" + cookie.getName() + cookie.getValue());

            }
        } finally {
            reponse.close();
            httpclient.close();
        }
        return result;
    }

    /**
     * post 发送文件方法
     * @author maxiaokui
     * @date 2021/10/26 16:07
     * @param message
     * @param 需要的入参参数
     * @return java.lang.String
     */

    public static String sendPostFile(String url,String req_data) throws IOException {
        String result = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse reponse = null;

        try {

            // A 得到httpclient对象和cookie存储区，用来发送请求
            // httpclient =HttpClients.createDefault();
            httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            // B 发送请求
            HttpPost httpPost = new HttpPost(url);

            //绑定数据和文件
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            File file = new File("D:\\\\case\\\\辅助_数据表接口模板.xlsx");
            multipartEntityBuilder.addBinaryBody("file",file);
            multipartEntityBuilder.addTextBody("productLineId", req_data);
            //multipartEntityBuilder.addTextBody("productLineId", "1456442877553364993");
            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);

            // 设置请求头和设置请求超时
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:87.0) Gecko/20100101 Firefox/87.0");
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000).build();
            httpPost.setConfig(requestConfig);

            // 会得到服务器返回值
            reponse = httpclient.execute(httpPost);
            // 得到服务器返回码
            int statuscode = reponse.getStatusLine().getStatusCode();

            // 判断返回码是否为200
            if (statuscode == HttpStatus.SC_OK) {
                // C 研究服务器返回值，但返回的不是字符串类型
                HttpEntity entity = reponse.getEntity();
                // System.out.println("我是post里的返回码"+statuscode);
                // E 研究EntityUtils，转换成字符串类型
                // result = EntityUtils.toString(entity, "utf-8");
                result = JSON.parseObject(EntityUtils.toString(entity, "utf-8")).toString();
                System.out.println("此处是" + HttpStatus.SC_OK + "里的返回值，方法是sendpost" + result);
            } else {
                result = "您的服务器返回码为" + statuscode + "请检查您的地址";
            }
            List<Cookie> listCookie = cookieStore.getCookies();
            for (Cookie cookie : listCookie) {
                System.out.println("cookie中的格式为" + cookie.getName() + cookie.getValue());

            }
        } finally {
            if(reponse != null){reponse.close();
                httpclient.close();}

        }
        return result;
    }

    /**
     * put请求方法
     * 
     * @author maxiaokui
     * @date 2021/10/26 16:07
     * @param
     * @param
     * @return java.lang.String
     */

    public static String sendPut(String url, String parm, String Req_Type) throws IOException {
        String result = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse reponse = null;

        try {
            // A 得到httpclient对象和cookie存储区，用来发送请求
            // httpclient =HttpClients.createDefault();
            httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            // B 发送请求
            HttpPut httpput = new HttpPut(url);
            // 设置请求头和设置请求超时
            httpRequestConfig(httpput, parm);
            System.out.println("sendput方法中的请求数据为：" + parm);
            // 请求时绑定数据
            httpput.setEntity(new StringEntity(parm, "utf-8"));

            // 会得到服务器返回值
            reponse = httpclient.execute(httpput);
            // 得到服务器返回码
            int statuscode = reponse.getStatusLine().getStatusCode();

            // 判断返回码是否为200
            if (statuscode == HttpStatus.SC_OK) {
                // C 研究服务器返回值，但返回的不是字符串类型
                HttpEntity entity = reponse.getEntity();
                // System.out.println("我是post里的返回码"+statuscode);
                // E 研究EntityUtils，转换成字符串类型
                // result = EntityUtils.toString(entity, "utf-8");
                result = JSON.parseObject(EntityUtils.toString(entity, "utf-8")).toString();
                System.out.println("此处是" + Req_Type + "里的返回值，方法是sendput" + result);
            } else {
                result = "PUT方法中的输出您的服务器返回码为" + statuscode + "请检查您的地址";
            }
            List<Cookie> listCookie = cookieStore.getCookies();
            for (Cookie cookie : listCookie) {
                System.out.println("cookie中的格式为" + cookie.getName() + cookie.getValue());

            }
        } finally {
            reponse.close();
            httpclient.close();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {

        // String a = sendGet(url,param);
        // System.out.println(a);

    }

}
