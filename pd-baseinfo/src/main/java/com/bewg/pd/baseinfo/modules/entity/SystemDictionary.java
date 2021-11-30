package com.bewg.pd.baseinfo.modules.entity;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 系统字典
 * @Author:
 * @Date: 2021-11-09
 * @Version: V1.0
 */
@Data
@TableName("t_dictionary")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "t_dictionary对象", description = "系统字典")
public class SystemDictionary implements Serializable {

    /** 主键 */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private Long id;
    /** 编码 */
    @ApiModelProperty(value = "编码")
    private java.lang.String dicCode;
    /** 名字 */
    @ApiModelProperty(value = "名字")
    private java.lang.String dicName;
    /** 值 */
    @ApiModelProperty(value = "值")
    private java.lang.String dicValue;
    /** 描述 */
    @ApiModelProperty(value = "描述")
    private java.lang.String dicDescribe;
    /** 父ID */
    @ApiModelProperty(value = "父ID")
    private Long parentId;
    /** 状态 0禁用 1使用 */
    @ApiModelProperty(value = "状态 0禁用 1使用")
    private java.lang.Integer dicStatus;
    /** isDel */
    @ApiModelProperty(value = "isDel")
    private java.lang.Integer isDel;
    /** 创建时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /** 修改时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

}
