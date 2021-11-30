package com.bewg.pd.common.entity.excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 图例
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("Legend")
public class Legend {

    @ApiModelProperty(value = "谨慎调整参数背景色")
    private String cautiousColor;

    @ApiModelProperty(value = "必须输入参数背景色")
    private String requiredColor;

    @ApiModelProperty(value = "报警图标")
    private String alarmIcon;

}
