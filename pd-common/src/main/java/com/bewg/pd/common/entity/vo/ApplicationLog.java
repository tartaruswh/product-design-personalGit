package com.bewg.pd.common.entity.vo;

import lombok.Data;

/**
 * 日志日志
 *
 * @author lizy
 */
@Data
public class ApplicationLog {
    /**
     * 项目编号
     */
    private String AppID;
    /**
     * 实例 ID，用来区别多实例，随机生成
     */
    private Long InstanceID;
    /**
     * 实例所属节点或容器 IP 地址，用于问题定位
     */
    private String InstanceIP;
    /**
     * 事件行为发生的时间（UTC 格式），例如 2021-03-18T05:23:37Z。
     */
    private String TimeStamp;
    /**
     * 操作对象名
     */
    private String ClassName;
    /**
     * 操作线程名
     */
    private String ThreadName;
    /**
     * 操作原因
     */
    private String Messages;
    /**
     * 日志类型
     */
    private String LogType;
    /**
     * 日志级别（级别从高到低 FATAL、ERROR、WARN、INFO、DEBUG、 TRACE、ALL
     */
    private String LogLevel;

}
