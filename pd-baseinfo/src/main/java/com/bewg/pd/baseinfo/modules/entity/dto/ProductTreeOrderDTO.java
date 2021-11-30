package com.bewg.pd.baseinfo.modules.entity.dto;

import java.util.HashMap;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品成员有序树
 *
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "产品成员有序树", description = "产品成员有序树")
public class ProductTreeOrderDTO {
    /** 上级主键ID */
    @ApiModelProperty(value = "上级主键ID")
    private Long parentId;
    /** 新序号 */
    @ApiModelProperty(value = "新序号")
    private HashMap<Long, Integer> orderMap;
}
