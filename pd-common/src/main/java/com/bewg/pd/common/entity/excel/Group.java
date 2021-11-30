package com.bewg.pd.common.entity.excel;

import com.bewg.pd.common.enums.GroupTypeEnum;
import com.bewg.pd.common.enums.StageEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * excel中的业务组
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Data
@ApiModel("Group")
public class Group {

    @ApiModelProperty(value = "业务组名")
    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "业务组类型")
    private GroupTypeEnum type;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "业务阶段")
    private StageEnum stage = StageEnum.UNKNOWN;

    @ApiModelProperty(value = "参数对象")
    private Parameter parameter;

    @ApiModelProperty(value = "配置清单表")
    private Table table;

    /**
     * 构造器
     */
    public Group() {
        parameter = new Parameter();
    }
}
