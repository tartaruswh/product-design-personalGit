package com.bewg.pd.baseinfo.modules.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 计算书模板和辅助数据关系表
 *
 * @author dongbd
 * @date 2021-11-09 15:58
 **/
@Data
@TableName("t_template_support_rel")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "t_template_support_rel对象", description = "计算书模板和辅助数据关系表")
public class TemplateSupportRel implements Serializable {

    /** 主键id */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
    private Long id;
    /** 辅助数据id */
    @ApiModelProperty(value = "辅助数据id")
    private Long supportId;
    /** 计算书模板id */
    @ApiModelProperty(value = "计算书模板id")
    private Long templateId;
    /** 创建时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private Long createBy;
}
