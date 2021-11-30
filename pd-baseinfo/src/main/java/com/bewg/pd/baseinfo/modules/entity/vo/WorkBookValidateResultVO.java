package com.bewg.pd.baseinfo.modules.entity.vo;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 计算书模板验证结果VO
 * 
 * @author dongbd
 * @date 2021/11/08 09:42
 **/
@Data
@ApiModel(value = "计算书模板验证结果VO", description = "计算书模板验证结果VO")
public class WorkBookValidateResultVO {
    /** 辅助数据表结果 */
    @ApiModelProperty(value = "辅助数据表结果")
    private List<Map<String, Boolean>> supportResult;
    /** 全局参数结果 */
    @ApiModelProperty(value = "全局参数结果")
    private List<Map<String, Boolean>> globalParameterResult;
}
