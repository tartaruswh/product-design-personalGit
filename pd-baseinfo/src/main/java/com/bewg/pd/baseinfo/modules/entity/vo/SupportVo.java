package com.bewg.pd.baseinfo.modules.entity.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zhaoyubo
 * @date 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "辅助数据表树返回对象", description = "辅助数据表树返回对象")
public class SupportVo {
    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String excelName;
    /** 文件地址 */
    @ApiModelProperty(value = "文件地址")
    private String excelUrl;
    /** 文件拓展名 */
    @ApiModelProperty(value = "文件拓展名")
    private String fileExtension;
    /** 所属产品线ID */
    @ApiModelProperty(value = "所属产品线ID")
    private Long productLineId;
    /** 被引用数量 */
    @ApiModelProperty(value = "被引用数量")
    private int refNumber;
    /** 创建时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /** 创建人员ID */
    @ApiModelProperty(value = "创建人员ID")
    private Long createBy;
    /** 最后更新时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;
    /** 最后更新人员ID */
    @ApiModelProperty(value = "最后更新人员ID")
    private Long updateBy;
    /** 乐观锁版本号 */
    @ApiModelProperty(value = "乐观锁版本号")
    private Integer optimistic;
    /** 逻辑删除：0未删除, 1已删除 */
    @ApiModelProperty(value = "逻辑删除：0未删除, 1已删除")
    private Integer isDel;
}
