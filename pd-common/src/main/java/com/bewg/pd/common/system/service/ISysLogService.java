package com.bewg.pd.common.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bewg.pd.common.system.entity.SysLog;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @Author lizy
 * @since 2021-10-19
 */
public interface ISysLogService extends IService<SysLog> {
    void addLog(String LogContent, Integer logType, Integer operateType);
}
