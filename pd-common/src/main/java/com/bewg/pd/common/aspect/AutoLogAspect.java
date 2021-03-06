package com.bewg.pd.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bewg.pd.common.aspect.annotation.AutoLog;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.system.entity.SysLog;
import com.bewg.pd.common.system.service.ISysLogService;
import com.bewg.pd.common.system.vo.LoginUser;
import com.bewg.pd.common.util.IPUtils;
import com.bewg.pd.common.util.SpringContextUtils;

/**
 * 系统日志，切面处理类
 * 
 * @Author lizy
 * @email lizy
 * @Date 2021-10-11
 */
@Aspect
@Component
public class AutoLogAspect {
    @Autowired
    private ISysLogService sysLogService;

    @Pointcut("@annotation(com.bewg.pd.common.aspect.annotation.AutoLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        long time = 0;
        Object result = null;
        // String message = new String();
        // 执行方法
        try {
            result = point.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            // 执行时长(毫秒)
            time = System.currentTimeMillis() - beginTime;
            // 保存日志
            saveSysLog(point, time);
        }
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if (syslog != null) {
            // 注解上的描述,操作日志内容
            sysLog.setLogContent(syslog.value());
            sysLog.setLogType(syslog.logType());

        }

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        // 设置操作类型
        if (sysLog.getLogType() == CommonConstant.LOG_TYPE_2) {
            sysLog.setOperateType(getOperateType(methodName, syslog != null ? syslog.operateType() : 0));
        }

        // 请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSONObject.toJSONString(args);
            sysLog.setRequestParam(params);
        } catch (Exception e) {

        }

        // 获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        // 设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));

        // 获取登录用户信息
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {

        }
        // 耗时
        sysLog.setCostTime(time);
        sysLog.setCreateTime(new Date());
        // 保存系统日志
        sysLogService.save(sysLog);
    }

    /**
     * 获取操作类型
     */
    private int getOperateType(String methodName, int operateType) {
        if (operateType > 0) {
            return operateType;
        }
        if (methodName.startsWith("list")) {
            return CommonConstant.OPERATE_TYPE_1;
        }
        if (methodName.startsWith("add")) {
            return CommonConstant.OPERATE_TYPE_2;
        }
        if (methodName.startsWith("edit")) {
            return CommonConstant.OPERATE_TYPE_3;
        }
        if (methodName.startsWith("delete")) {
            return CommonConstant.OPERATE_TYPE_4;
        }
        if (methodName.startsWith("import")) {
            return CommonConstant.OPERATE_TYPE_5;
        }
        if (methodName.startsWith("export")) {
            return CommonConstant.OPERATE_TYPE_6;
        }
        return CommonConstant.OPERATE_TYPE_1;
    }
}
