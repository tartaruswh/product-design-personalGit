package com.bewg.pd.baseinfo.modules.entity.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模板发布所需参数
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "模板发布所需参数", description = "模板发布所需参数")
public class ReleaseTemplateDTO implements Serializable {
    /** 计算书模板Id */
    @ApiModelProperty(value = "计算书模板Id")
    private Long id;
    /** 版本说明 */
    @ApiModelProperty(value = "版本说明")
    private String versionDescription;
    /** 创建人编号 */
    @ApiModelProperty(value = "创建人编号")
    private Long createBy;
    /** 保留的导图Id集合 */
    @ApiModelProperty(value = "保留的导图Id集合")
    private List<Long> imageIdList;
    /** 保留的附件Id */
    @ApiModelProperty(value = "保留的附件Id")
    private Long annexId;
}
