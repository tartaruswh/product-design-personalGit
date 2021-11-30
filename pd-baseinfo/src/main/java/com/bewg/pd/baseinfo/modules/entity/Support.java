package com.bewg.pd.baseinfo.modules.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 辅助数据表
 * @Author: Zhaoyubo
 * @Date: 2021-10-19
 * @Version: V1.0
 */
@Data
@TableName("t_support_workbook")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "t_support_workbook对象", description = "辅助数据表")
public class Support implements Serializable {

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String excelName;
    /** 文件拓展名 */
    @ApiModelProperty(value = "文件拓展名")
    private String fileExtension;
    /** 文件地址 */
    @ApiModelProperty(value = "文件地址")
    private String excelUrl;
    /** 所属产品线ID */
    @ApiModelProperty(value = "所属产品线ID")
    private Long productLineId;
    /** 当前批次号 */
    @ApiModelProperty(value = "当前批次号")
    private Date batchid;
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
