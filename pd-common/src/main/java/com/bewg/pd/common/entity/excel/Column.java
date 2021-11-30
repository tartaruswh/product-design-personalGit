package com.bewg.pd.common.entity.excel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * excel中的列
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("Column")
public class Column {

    @ApiModelProperty(value = "列名")
    private String name;

    @ApiModelProperty(value = "列索引, 用于行数据取值", hidden = true)
    @JsonIgnore
    private int index;

}
