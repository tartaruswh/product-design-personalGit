package com.bewg.pd.baseinfo.modules.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 验证辅助数据表结果VO
 * 
 * @author dongbd
 * @date 2021/11/09 11:04
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "验证辅助数据表结果VO", description = "验证辅助数据表结果VO")
public class SupportValidateVO {
    /** 是否验证通过 */
    @ApiModelProperty(value = "是否验证通过")
    private Integer isVerified;
    @ApiModelProperty(value = "名称：结果")
    private Map<String, Integer> resultMap;
}
