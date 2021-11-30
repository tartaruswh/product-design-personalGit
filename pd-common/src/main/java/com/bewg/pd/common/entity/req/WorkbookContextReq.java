package com.bewg.pd.common.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 计算书上下文创建实体
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("WorkbookContextReq")
public class WorkbookContextReq implements Serializable {

    @NotBlank(message = "单体编号不可为空!")
    @ApiModelProperty(value = "单体编号")
    private String productMemberId;

    @NotBlank(message = "模型文档路径不可为空!")
    @ApiModelProperty(value = "模型文档路径")
    private String docPath;

    @NotBlank(message = "模型文档名称不可为空!")
    @ApiModelProperty(value = "模型文档名称")
    private String docName;

    @NotEmpty(message = "关联文档路径不可为空!")
    @ApiModelProperty(value = "关联文档路径")
    private List<ParamPair> associatedDocPath;

    @Data
    @ApiModel("WorkbookContextReq.ParamPair")
    public static class ParamPair {

        @ApiModelProperty(value = "文档名称")
        String docName;

        @ApiModelProperty(value = "文档路径")
        String docPath;
    }

}

