package com.bewg.pd.baseinfo.modules.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bewg.pd.baseinfo.modules.entity.vo.SysUserVo;
import com.bewg.pd.common.entity.po.SysUser;

/**
 * @Description: 用户信息
 * @Author: lizy
 * @Date: 2019-12-15
 * @Version: V1.0
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 获取用户角色
     * 
     * @return
     */
    List<SysUserVo.SysRole> getUserRoles();

    /**
     * 获取用户机构
     * 
     * @return
     */
    List<SysUserVo.SysOrg> getUserOrgs();

    @Select({"<script>", "select distinct su.id,su.username,su.realname ", "from t_sys_user su,t_sys_user_org suo ", "where suo.user_id = su.id ", "and su.status = 1 ", "and su.del_flag = '0' ", "and suo.org_id in",
        "<foreach collection='orgIdList' item='id' open='(' separator=',' close=')'>", "#{id}", "</foreach>", "</script>",})
    List<SysUser> getOrgUsers(@Param("orgIdList") List<String> orgIdList);

    @Select({"<script>", "select distinct su.id,su.username,su.realname ", "from t_sys_user su,t_sys_user_org suo ", "where suo.user_id = su.id ", "and su.status = 1 ", "and su.del_flag = '0' ", "and suo.org_id in ",
        "<foreach collection='orgIdList' item='id' open='(' separator=',' close=')'> ", "#{id}", "</foreach>", "and su.id in ( ", "select distinct su.id ", "from t_sys_user su,t_sys_user_role sur,t_sys_role sr ",
        "where sur.user_id = su.id and sr.id = sur.role_id ", "and sr.role_code in ", "<foreach collection='roleCodeList' item='id' open='(' separator=',' close=')'>", "#{id}", "</foreach>", ") ", "</script>",})
    List<SysUser> getOrgUsersWithRoles(@Param("orgIdList") List<String> orgIdList, @Param("roleCodeList") List<String> roleCodeList);

    @Select({"select su.id,su.realname,su.username from t_sys_user su,t_sys_role ro,t_sys_user_role sur " + "where sur.user_id = su.id and sur.role_id = ro.id " + "and su.del_flag = 0 and su.status = 1 and ro.role_code =#{roleCode} "})
    List<SysUser> getUsersByRoleCode(@Param("roleCode") String roleCode);
}
