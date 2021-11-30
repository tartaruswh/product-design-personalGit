package com.bewg.pd.baseinfo.modules.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewg.pd.baseinfo.modules.entity.vo.SysUserVo;
import com.bewg.pd.baseinfo.modules.mapper.SysUserMapper;
import com.bewg.pd.baseinfo.modules.service.ISysUserService;
import com.bewg.pd.common.entity.po.SysUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 用户信息
 * @Author: lizy
 * @Date: 2019-12-15
 * @Version: V1.0
 */
@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public SysUser getUserByName(String username) {
        return null;
    }

    @Override
    public SysUser getUserByPhone(String phone) {
        return null;
    }

    @Override
    public Set<String> getUserRolesSet(String userId) {
        return null;
    }

    @Override
    public Map<String, SysUser> getUserByIdSet(Set<String> userIdSet) {
        return null;
    }

    @Override
    public List<SysUser> getUserListByOrgId(String orgId, boolean containChild) {
        return null;
    }

    @Override
    public Set<String> getUserPermissionsSet(String userId) {
        return null;
    }

    @Override
    public void add(SysUser sysUser, String roleIds, String orgIds) {

    }

    @Override
    public void edit(SysUser sysUser, String roleIds, String orgIds) {

    }

    @Override
    public List<SysUserVo> getUserRoles(QueryParams params) {
        return null;
    }

    @Override
    public void delete(String ids) {

    }

    @Override
    public void changePassword(UpdateParams params) {

    }

    @Override
    public void batchFrozen(String ids, Integer status) {

    }

    @Override
    public SysUser checkUserIsEffective(SysUser sysUser) {
        return null;
    }

    @Override
    public void updatePassword(UpdateParams params) {

    }

    @Override
    public List<SysUser> getUserListByOrgIdAndRoleCode(String orgId, String roleCodes, boolean containChild) {
        return null;
    }

    @Override
    public List<SysUser> getUsersByRoleCode(String roleCode) {
        return null;
    }

    @Override
    public void createMessage(SysUser sysUser) {

    }
}
