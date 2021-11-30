package com.bewg.pd.baseinfo.modules.entity.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 删除计算书版本所需参数
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "删除计算书版本所需参数", description = "删除计算书版本所需参数")
public class RemoveTemplateDTO implements Serializable {
    /** 计算书模板Id */
    @ApiModelProperty(value = "计算书模板Id")
    private Long id;
    /** 单体类型Id */
    @ApiModelProperty(value = "单体类型Id")
    private Long productMemberId;
    /** 新增/编辑类型(ADD/EDIT) */
    @ApiModelProperty(value = "新增/编辑类型(ADD/EDIT)")
    private String btnType;
    /** 创建人编号 */
    @ApiModelProperty(value = "创建人编号")
    private Long createBy;
}
