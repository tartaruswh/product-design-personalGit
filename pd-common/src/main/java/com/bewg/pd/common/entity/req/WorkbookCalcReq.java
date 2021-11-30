package com.bewg.pd.common.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 计算书驱动计算参数实体
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("WorkbookCalcReq")
public class WorkbookCalcReq implements Serializable {

    @NotBlank(message = "上下文编号不可为空!")
    @ApiModelProperty(value = "上下文编号")
    private String contextId;

    @NotNull(message = "输入参数不可为空!")
    @ApiModelProperty(value = "输入参数")
    private ParamPair reqParam;

    @Data
    @ApiModel("WorkbookCalcReq.ParamPair")
    public static class ParamPair {

        @ApiModelProperty(value = "参数值")
        private String val;

        @ApiModelProperty(value = "坐标, 例如: B4")
        private String coordinate;

        @ApiModelProperty(value = "是否为必输项")
        private boolean required;
    }
}

