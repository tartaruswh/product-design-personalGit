package com.bewg.pd.common.entity.excel;

import com.bewg.pd.common.enums.DirectionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 判断性参数对象
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("ParameterJ")
public class ParameterJ {

    @ApiModelProperty(value = "参数名")
    private String paramName;

    @ApiModelProperty(value = "参数值")
    private Object value;

    @ApiModelProperty(value = "是否报警")
    private boolean isAlarm;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "备注")
    private String comment;

    @ApiModelProperty(value = "坐标")
    private String coordinate;

    @ApiModelProperty(value = "最小值")
    private BigDecimal minimum;

    @ApiModelProperty(value = "最大值")
    private BigDecimal maximum;
}
