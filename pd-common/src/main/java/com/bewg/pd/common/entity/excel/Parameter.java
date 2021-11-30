package com.bewg.pd.common.entity.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 参数对象
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("Parameter")
public class Parameter {

    @ApiModelProperty(value = "普通参数列表")
    private List<ParameterC> parameterC;

    @ApiModelProperty(value = "判断性参数列表")
    private List<ParameterJ> parameterJ;

    /**
     * 构造器
     */
    public Parameter() {
        parameterC = new ArrayList<>();
        parameterJ = new ArrayList<>();
    }

}
