package com.bewg.pd.common.system.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewg.pd.common.system.entity.SysLog;
import com.bewg.pd.common.system.mapper.SysLogMapper;
import com.bewg.pd.common.system.service.ISysLogService;
import com.bewg.pd.common.system.vo.LoginUser;
import com.bewg.pd.common.util.IPUtils;
import com.bewg.pd.common.util.SpringContextUtils;

import lombok.RequiredArgsConstructor;

/**
 * @author lizy
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public void addLog(String logContent, Integer logType, Integer operateType) {
        SysLog sysLog = new SysLog();
        // 注解上的描述,操作日志内容
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operateType);
        try {
            // 获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            // 设置IP地址
            sysLog.setIp(IPUtils.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        sysLog.setCreateTime(new Date());
        // 保存系统日志
        baseMapper.insert(sysLog);
    }
}
