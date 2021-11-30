package com.bewg.pd.baseinfo.modules.entity.vo;

import java.util.Date;
import java.util.List;

import com.bewg.pd.common.entity.excel.Sheet;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 历史版本解析VO
 * 
 * @author dongbd
 * @date 2021/11/19 13:58
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "历史版本解析VO", description = "历史版本解析VO")
public class WorkBookParseVO {
    /** 导图集合 */
    @ApiModelProperty(value = "导图集合")
    private List<AttachedFileVO> imageList;
    /** 计算后的sheet页 */
    @ApiModelProperty(value = "计算后的sheet页")
    private Sheet sheet;
}
