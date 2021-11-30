package com.bewg.pd.common.entity.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * excel中的行
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("Row")
public class Row {

    @ApiModelProperty(value = "行内单元格值集合")
    private List<String> values;

    /**
     * 构造器
     */
    public Row() {
        values = new ArrayList<>();
    }

}
