package com.bewg.pd.baseinfo.modules.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lizy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "批量删除参数", description = "批量删除参数")
public class IdsReqParameter {
    /** 编号数组 */
    @ApiModelProperty(value = "ID")
    private java.lang.Integer[] ids;
}
