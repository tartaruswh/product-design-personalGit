package com.bewg.pd.common.entity.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * excel中的表格
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@Slf4j
public class Table {

    @ApiModelProperty(value = "表格名称")
    private String name;

    @ApiModelProperty(value = "表头")
    private List<Column> columns;

    @ApiModelProperty(value = "表格中的行")
    private List<Row> rows;

    /**
     * 构造器
     */
    public Table() {
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }

}
