package com.bewg.pd.common.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 计算书处理参数实体
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("WorkbookFileReq")
public class WorkbookFileReq implements Serializable {

    @NotBlank(message = "计算书文件路径不可为空!")
    @ApiModelProperty(value = "计算书文件路径(下载使用)")
    private String filePath;

}

