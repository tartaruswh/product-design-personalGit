package com.bewg.pd.common.aspect;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author siyuan926
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAspect {
    private static final String ENCODING = "utf-8";

    /**
     * 定义切点Pointcut
     */
    @Pointcut("execution(*  *..*.*.controller..*.*(..))")
    public void executeService() {}

    /**
     * 执行切点之前
     * 
     * @param pjp
     */
    @Before("executeService()")
    public void exBefore(JoinPoint pjp) {}

    /**
     * 通知（环绕）
     * 
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("executeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        Object[] args = pjp.getArgs();
        String params = "";
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        try {
            // 获取请求参数集合并进行遍历拼接
            if (args.length > 0) {
                if ("POST".equals(method)) {
                    try {
                        params = JSONArray.toJSONString(args);
                    } catch (Exception e) {
                        log.info("JSONArray.toJSONString异常:{}", e.getMessage());
                    }
                } else if ("GET".equals(method)) {
                    params = queryString;
                }
                if (params != null) {
                    params = URLDecoder.decode(params, ENCODING);
                }
            }
            log.info("请求参数为 ：{}", params);
            log.info("响应结果为:" + JSONObject.toJSONString(result));
        } catch (Exception e) {
            log.info("响应结果JSON格式化错误：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行切点之后
     * 
     * @param joinPoint
     */
    @After("executeService()")
    public void exAfter(JoinPoint joinPoint) {}
}