package com.bewg.pd.common.entity.excel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * excel中的sheet页
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("计算后的sheet页")
@JsonIgnoreProperties(value = {""})
public class Sheet {

    @ApiModelProperty(value = "业务分组")
    private List<Group> groups;

    @ApiModelProperty(value = "图例")
    private Legend legend;

    @ApiModelProperty(value = "配置清单表", hidden = true)
    private int tableStartIndex;

}
