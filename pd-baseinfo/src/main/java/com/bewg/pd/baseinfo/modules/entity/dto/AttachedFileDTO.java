package com.bewg.pd.baseinfo.modules.entity.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模板附属文件
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "上传附属文件所需参数", description = "上传附属文件所需参数")
public class AttachedFileDTO implements Serializable {
    /** 文件类型 */
    @ApiModelProperty(value = "文件类型")
    private String type;
    /** 计算书模板Id */
    @ApiModelProperty(value = "计算书模板Id")
    private Long templateWorkbookId;
    /** 单体类型Id */
    @ApiModelProperty(value = "单体类型Id")
    private Long productMemberId;
    /** 发布状态 */
    @ApiModelProperty(value = "发布状态")
    private Integer releaseStatus;
    /** 创建人编号 */
    @ApiModelProperty(value = "创建人编号")
    private Long createBy;
}
