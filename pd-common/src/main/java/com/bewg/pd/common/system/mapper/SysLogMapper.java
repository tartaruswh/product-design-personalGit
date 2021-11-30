package com.bewg.pd.common.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bewg.pd.common.system.entity.SysLog;

/**
 * <p>
 * 系统日志表 Mapper 接口
 * </p>
 *
 * @Author lizy
 * @since 2021-10-19
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {}
