package com.bewg.pd.baseinfo.modules.entity.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 计算书版本列表返回
 * 
 * @author Zhaoyubo
 * @date 2021/11/03 21:58
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "计算书版本列表返回", description = "计算书版本列表返回")
public class TemplateWorkbookVO {
    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 是否有效, 默认为否 */
    @ApiModelProperty(value = "是否有效, 默认为否")
    private Integer isCandidate;
    /** 是否验证通过, 默认为否 */
    @ApiModelProperty(value = "是否验证通过, 默认为否")
    private Integer isVerified;
    /** 文件名称 */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /** 文件拓展名 */
    @ApiModelProperty(value = "文件拓展名")
    private String fileExtension;
    /** 版本说明 */
    @ApiModelProperty(value = "版本说明")
    private String versionDescription;
    /** 单体编号 */
    @ApiModelProperty(value = "单体编号")
    private Long productMemberId;
    /** 模型文档路径 */
    @ApiModelProperty(value = "模型文档路径")
    private String filePath;
    /** 版本号 */
    @ApiModelProperty(value = "版本号")
    private String version;
    /** 发布状态 */
    @ApiModelProperty(value = "发布状态")
    private Integer releaseStatus;
    /** 被引用数量 */
    @ApiModelProperty(value = "被引用数量")
    private int refNumber;
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
}
