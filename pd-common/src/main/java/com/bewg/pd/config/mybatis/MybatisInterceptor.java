package com.bewg.pd.config.mybatis;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import com.bewg.pd.common.util.oConvertUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 * 
 * @author lizy
 * @Date 2019-12-14
 *
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("------sqlId------" + sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        log.debug("------sqlCommandType------" + sqlCommandType);

        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType) {
            Field[] fields = oConvertUtils.getAllFields(parameter);
            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    // 获取登录用户信息
                    // LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                    if ("createUser".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createBy = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createBy == null || local_createBy.equals("")) {
                            String createBy = "";
                            // if (sysUser != null) {
                            // // 登录账号
                            // createBy = sysUser.getId();
                            // }
                            // if (StringUtils.isNotBlank(createBy)) {
                            // field.setAccessible(true);
                            // field.set(parameter, createBy);
                            // field.setAccessible(false);
                            // }
                        }
                    }
                    if ("createUserName".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createUserName = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createUserName == null || local_createUserName.equals("")) {
                            String createUserName = "";
                            // if (sysUser != null) {
                            // // 登录账号
                            // createUserName = sysUser.getUsername();
                            // }
                            // if (StringUtils.isNotBlank(createUserName)) {
                            // field.setAccessible(true);
                            // field.set(parameter, createUserName);
                            // field.setAccessible(false);
                            // }
                        }
                    }
                    // 注入创建时间
                    if ("createTime".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createDate = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createDate == null || local_createDate.equals("")) {
                            field.setAccessible(true);
                            field.set(parameter, new Date());
                            field.setAccessible(false);
                        }
                    }
                    // 注入部门编码
                    if ("sysOrgCode".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_sysOrgCode = field.get(parameter);
                        field.setAccessible(false);
                        if (local_sysOrgCode == null || local_sysOrgCode.equals("")) {
                            String sysOrgCode = "";
                            // 获取登录用户信息
                            // if (sysUser != null) {
                            // // 登录账号
                            // if (sysUser.getSysOrgList() != null && sysUser.getSysOrgList().size() > 0) {
                            // sysOrgCode = sysUser.getSysOrgList().get(0).getOrgId();
                            // }
                            // }
                            // if (StringUtils.isNotBlank(sysOrgCode)) {
                            // field.setAccessible(true);
                            // field.set(parameter, sysOrgCode);
                            // field.setAccessible(false);
                            // }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        if (SqlCommandType.UPDATE == sqlCommandType) {
            Field[] fields = null;
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>)parameter;
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                } else {
                    parameter = p.get("param1");
                }
                if (parameter == null) {
                    return invocation.proceed();
                }
                fields = oConvertUtils.getAllFields(parameter);
            } else {
                fields = oConvertUtils.getAllFields(parameter);
            }

            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    if ("updateBy".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_updateBy = field.get(parameter);
                        field.setAccessible(false);
                        if (local_updateBy == null || local_updateBy.equals("")) {
                            String updateBy = "";
                            // 获取登录用户信息
                            // LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                            // if (sysUser != null) {
                            // // 登录账号
                            // updateBy = sysUser.getId();
                            // }
                            // if (StringUtils.isNotBlank(updateBy)) {
                            // field.setAccessible(true);
                            // field.set(parameter, updateBy);
                            // field.setAccessible(false);
                            // }
                        }
                    }
                    if ("updateTime".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_updateDate = field.get(parameter);
                        field.setAccessible(false);
                        if (local_updateDate == null || local_updateDate.equals("")) {
                            field.setAccessible(true);
                            field.set(parameter, new Date());
                            field.setAccessible(false);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
