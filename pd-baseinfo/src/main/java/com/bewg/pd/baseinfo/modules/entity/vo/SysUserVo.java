package com.bewg.pd.baseinfo.modules.entity.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户信息VO
 * @Author: lizy
 * @Date: 2021-11-12
 * @Version: V1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserVo implements Serializable {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "登录账号")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "性别（1：男 2：女）")
    private Integer sex;

    @ApiModelProperty(value = "电子邮件")
    private String email;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "职务")
    private String post;

    @ApiModelProperty(value = "状态(1：正常  2：冻结)")
    private Integer status;

    @ApiModelProperty(value = "同步工作流引擎1同步0不同步")
    private String activitiSync;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注（不超过60个汉字）")
    private String remark;

    @ApiModelProperty(value = "用户类型（1-集团用户 0 外部用户）")
    private Integer type;

    @ApiModelProperty(value = "角色")
    List<SysRole> sysRoleList;

    @ApiModelProperty(value = "机构")
    List<SysOrg> sysOrgList;

    private String orgName;

    private String roleName;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public static class SysRole {
        @ApiModelProperty(value = "用户Id")
        private String userId;

        @ApiModelProperty(value = "角色Id")
        private String id;

        @ApiModelProperty(value = "角色名")
        private String roleName;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public static class SysOrg {
        @ApiModelProperty(value = "用户Id")
        private String userId;

        @ApiModelProperty(value = "机构Id")
        private String id;

        @ApiModelProperty(value = "机构名")
        private String orgName;

        @ApiModelProperty(value = "机构code")
        private String orgCode;

        @ApiModelProperty(value = "机构类型")
        private Integer orgType;

    }
}
