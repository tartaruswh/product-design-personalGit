package com.bewg.pd.common.entity.po;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bewg.pd.common.dict.DEL_FLAG;
import com.bewg.pd.common.validator.EnumValue;
import com.bewg.pd.common.validator.InsertValidate;
import com.bewg.pd.common.validator.UpdateValidate;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户信息
 * @Author: lizy
 * @Date: 2019-12-15
 * @Version: V1.0
 */

@Data
@TableName("t_sys_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "t_sys_user对象", description = "用户信息")
public class SysUser implements Serializable {

    /** 主键id */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Long id;

    /** 登录账号 */
    @ApiModelProperty(value = "登录账号")
    @Pattern(regexp = "^[A-Za-z0-9\\-_.]+$", message = "用户名只能包括大小写字母、短线、点、下划线和数字", groups = {InsertValidate.class, UpdateValidate.class})
    @NotBlank(message = "登录账号不能为空", groups = {InsertValidate.class, UpdateValidate.class})
    @Length(max = 100, message = "登录账号最多100位")
    private String username;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名")
    @Pattern(regexp = "^[\u4E00-\u9FA5A-Za-z0-9_\\-.]+$", message = "姓名只能包括汉字、短线、点、大小写字母和数字", groups = {InsertValidate.class, UpdateValidate.class})
    @NotBlank(message = "真实姓名不能为空", groups = {InsertValidate.class, UpdateValidate.class})
    @Length(max = 100, message = "真实姓名最多100位")
    private String realname;

    /** 密码 */
    @ApiModelProperty(value = "密码")
    @Length(min = 6, message = "密码最小长度6位", groups = {InsertValidate.class})
    @NotBlank(message = "密码不能为空", groups = {InsertValidate.class})
    private String password;

    /** md5密码盐 */
    @ApiModelProperty(value = "md5密码盐")
    private String salt;

    /** 头像 */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /** 生日 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生日")
    private Date birthday;

    /** 性别（1：男 2：女） */
    @ApiModelProperty(value = "性别（1：男 2：女）")
    @NotNull(message = "请输入性别")
    @Range(min = 1, max = 2, message = "性别只能为（1：男 2：女）")
    private Integer sex;

    /** 电子邮件 */
    @ApiModelProperty(value = "电子邮件")
    private String email;

    /** 电话 */
    @ApiModelProperty(value = "电话")
    @NotBlank(message = "电话不能为空", groups = {InsertValidate.class, UpdateValidate.class})
    @Pattern(regexp = "^(13|14|15|16|17|18|19){1}\\d{9}$", groups = {InsertValidate.class, UpdateValidate.class}, message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty(value = "职务")
    private String post;

    /** 状态(1：正常 2：冻结 ） */
    @ApiModelProperty(value = "状态(1：正常  2：冻结)")
    @Range(min = 1, max = 2, message = "状态只能为（1：正常 2：冻结)", groups = {InsertValidate.class, UpdateValidate.class})
    private Integer status;

    /** 删除状态（0，正常，1已删除） */
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    @TableLogic
    @EnumValue(enumClass = DEL_FLAG.class, groups = {InsertValidate.class, UpdateValidate.class}, message = "删除状态只能为（0，正常，1已删除）")
    private String delFlag;

    /** 同步工作流引擎1同步0不同步 */
    @ApiModelProperty(value = "同步工作流引擎1同步0不同步")
    private String activitiSync;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /** 备注 */
    @ApiModelProperty(value = "备注（不超过60个汉字）")
    @Length(max = 200, message = "备注长度最多200位")
    private String remark;

    @ApiModelProperty(value = "用户类型（1-集团用户 0 外部用户）")
    @Range(min = 0, max = 1, message = "用户类型只能为（1-集团用户 0 外部用户）", groups = {InsertValidate.class, UpdateValidate.class})
    private Integer type;

}
