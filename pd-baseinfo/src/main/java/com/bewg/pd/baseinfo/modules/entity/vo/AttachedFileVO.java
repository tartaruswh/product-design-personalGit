package com.bewg.pd.baseinfo.modules.entity.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模板附属文件表
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "t_attached_file对象", description = "模板附属文件表")
public class AttachedFileVO implements Serializable {
    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 文件类型 */
    @ApiModelProperty(value = "文件类型")
    private String type;
    /** 文件名称 */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /** 附属文件路径 */
    @ApiModelProperty(value = "附属文件路径")
    private String path;
}
