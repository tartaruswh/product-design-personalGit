package com.bewg.pd.common.entity.excel;

import com.bewg.pd.common.enums.DirectionEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 普通参数对象
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("ParameterC")
public class ParameterC {

    @ApiModelProperty(value = "参数名")
    private String paramName;

    @ApiModelProperty(value = "参数值")
    private Object value;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "是否为公式项")
    private boolean isFormula = false;

    @ApiModelProperty(value = "公式项")
    private String formulaValue;

    @ApiModelProperty(value = "坐标")
    private String coordinate;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "参数方向")
    private DirectionEnum direction = DirectionEnum.UNKNOWN;

    @ApiModelProperty(value = "是否为谨慎调整项")
    private boolean cautious = false;

    @ApiModelProperty(value = "是否为必须输入参数")
    private boolean required = false;

}
