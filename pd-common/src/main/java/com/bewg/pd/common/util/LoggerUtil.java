package com.bewg.pd.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bewg.pd.common.entity.vo.ApplicationLog;

/**
 * 日志封装
 *
 * @author lizy
 */
@Component
public class LoggerUtil {

    public static LoggerUtil logger;
    @Value("${workbook.projectinfo.projectid}")
    private String projectId;
    public static ApplicationLog appLog = new ApplicationLog();

    @PostConstruct
    public void init() {
        logger = this;
        // 项目编号：BEWG_workbook_10001
        appLog.setAppID(logger.projectId);
        // 实例 ID，用来区别多实例，随机生成
        appLog.setInstanceID(new IdWorkerUtil().nextId());
        // 实例所属节点或容器 IP 地址，用于问题定位
        try {
            appLog.setInstanceIP(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // 事件行为发生的时间（UTC 格式），例如 2021-03-18T05:23:37Z。
        appLog.setTimeStamp(DateUtils.nowUTC());
    }

    private static void setLogObj(String msg, Object obj, String logType, String logLevel) {
        // 操作对象名
        appLog.setClassName(obj.getClass().getName());
        // 操作线程名
        appLog.setThreadName(Thread.currentThread().getName());
        // 操作原因
        appLog.setMessages(msg);
        // 日志类型
        appLog.setLogType(logType);
        // 日志级别（级别从高到低 FATAL、ERROR、WARN、INFO、DEBUG、 TRACE、ALL）
        appLog.setLogLevel(logLevel);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerUtil.class);

    public static void error(String msg) {
        LoggerFactory.getLogger(getClassName()).error(msg);
    }

    public static void error(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).error(msg, obj);
    }

    public static void error(String msg, Object obj, String logType) {
        setLogObj(msg, obj, logType, "DEBUG");
        LOGGER.info(JSON.toJSONString(appLog));
    }

    public static void warn(String msg) {
        LoggerFactory.getLogger(getClassName()).error(msg);
    }

    public static void warn(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).error(msg, JSON.toJSONString(obj));
    }

    public static void warn(String msg, Object obj, String logType) {
        setLogObj(msg, obj, logType, "DEBUG");
        LOGGER.info(JSON.toJSONString(appLog));
    }

    public static void info(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).info(msg, obj);
    }

    public static void info(String msg, Object obj, String logType) {
        setLogObj(msg, obj, logType, "INFO");
        LOGGER.info(JSON.toJSONString(appLog));
    }

    public static void debug(String msg) {
        LoggerFactory.getLogger(getClassName()).debug(msg);
    }

    public static void debug(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).debug(msg, obj);
    }

    public static void debug(String msg, Object obj, String logType) {
        setLogObj(msg, obj, logType, "DEBUG");
        LOGGER.info(JSON.toJSONString(appLog));
    }

    /**
     * 获取调用 error,info,debug静态类的类名
     */
    private static String getClassName() {
        return new SecurityManager() {
            public String getClassName() {
                return getClassContext()[3].getName();
            }
        }.getClassName();
    }
}
