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
 * 模板附属文件表
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
@Data
@TableName("t_attached_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "t_attached_file对象", description = "模板附属文件表")
public class AttachedFile implements Serializable {

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 文件名称 */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /** 文件类型 */
    @ApiModelProperty(value = "文件类型")
    private String type;
    /** 文件拓展名 */
    @ApiModelProperty(value = "文件拓展名")
    private String fileExtension;
    /** 文件大小 */
    @ApiModelProperty(value = "文件大小")
    private String fileSize;
    /** 附属文件路径 */
    @ApiModelProperty(value = "附属文件路径")
    private String path;
    /** 计算书模板Id */
    @ApiModelProperty(value = "计算书模板Id")
    private Long templateWorkbookId;
    /** 单体编号 */
    @ApiModelProperty(value = "单体编号")
    private Long productMemberId;
    /** 发布状态 */
    @ApiModelProperty(value = "发布状态")
    private Integer releaseStatus;
    /** 创建人编号 */
    @ApiModelProperty(value = "创建人编号")
    private Long createBy;
    /** 更新人编号 */
    @ApiModelProperty(value = "更新人编号")
    private Long updateBy;
    /** 创建时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /** 修改时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /** 逻辑删除：0未删除, 1已删除 */
    @ApiModelProperty(value = "逻辑删除：0未删除, 1已删除")
    private Integer isDel;
    /** 乐观锁版本号 */
    @ApiModelProperty(value = "乐观锁版本号")
    private Integer optimistic;
}
