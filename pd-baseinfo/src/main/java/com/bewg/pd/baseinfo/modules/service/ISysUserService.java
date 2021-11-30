package com.bewg.pd.baseinfo.modules.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bewg.pd.baseinfo.modules.entity.vo.SysUserVo;
import com.bewg.pd.common.entity.po.SysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @Description: 用户信息
 * @Author: lizy
 * @Date: 2019-12-15
 * @Version: V1.0
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser getUserByName(String username);

    SysUser getUserByPhone(String phone);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param userId
     *            用户ID
     * @return 角色集合
     */
    Set<String> getUserRolesSet(String userId);

    /**
     * 查询所有用户集合,包括用户ID,用户username,realName
     * 
     * @param userIdSet
     * @return
     */
    Map<String, SysUser> getUserByIdSet(Set<String> userIdSet);

    /**
     * 查询指定机构的所有用户
     * 
     * @param orgId
     *            机构id
     * @param containChild
     *            是否包含下级机构
     * @return
     */
    List<SysUser> getUserListByOrgId(String orgId, boolean containChild);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param userId
     *            用户ID
     * @return 权限集合
     */
    Set<String> getUserPermissionsSet(String userId);

    /**
     * 新增用户
     * 
     * @param sysUser
     *            用户
     * @param roleIds
     *            角色
     * @param orgIds
     *            机构
     */
    void add(SysUser sysUser, String roleIds, String orgIds);

    /**
     * 修改
     * 
     * @param sysUser
     *            用户
     * @param roleIds
     *            角色
     * @param orgIds
     *            机构
     */
    void edit(SysUser sysUser, String roleIds, String orgIds);

    /**
     * 查询用户及角色（一对多）
     * 
     * @return
     */
    List<SysUserVo> getUserRoles(QueryParams params);

    /**
     * 删除多个id逗号隔开
     * 
     * @param ids
     */
    void delete(String ids);

    /**
     * 修改重置用户名密码
     */
    void changePassword(UpdateParams params);

    /**
     * 用户批量冻结/解冻
     * 
     * @param ids
     * @param status
     */
    void batchFrozen(String ids, Integer status);

    /**
     * 校验用户有效性
     * 
     * @param sysUser
     */
    SysUser checkUserIsEffective(SysUser sysUser);

    /**
     * 更新密码
     * 
     * @param params
     */
    void updatePassword(UpdateParams params);

    List<SysUser> getUserListByOrgIdAndRoleCode(String orgId, String roleCodes, boolean containChild);

    List<SysUser> getUsersByRoleCode(String roleCode);

    void createMessage(SysUser sysUser);

    @Data
    @EqualsAndHashCode
    @Accessors(chain = true)
    @ApiModel(value = "用户管理.查询参数")
    class QueryParams {

        @ApiModelProperty(value = "关键字")
        private String keyword;

    }

    @Data
    @EqualsAndHashCode
    @Accessors(chain = true)
    @ApiModel(value = "修改用户密码")
    @FieldNameConstants
    class UpdateParams {

        @ApiModelProperty(value = "ID")
        private String id;

        @ApiModelProperty(value = "旧密码")
        private String oldPassword;

        @ApiModelProperty(value = "新密码")
        private String password;

        @ApiModelProperty(value = "确认新密码")
        private String confirmPassword;

    }

    @Data
    @EqualsAndHashCode
    @Accessors(chain = true)
    @ApiModel(value = "验证用户验证码")
    @FieldNameConstants
    class VerifyParams {

        @ApiModelProperty(value = "手机号")
        private String phone;

        @ApiModelProperty(value = "验证码")
        private String verifyCode;

    }
}
