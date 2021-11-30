package com.bewg.pd.baseinfo.modules.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 历史版本详情VO
 * 
 * @author dongbd
 * @date 2021/11/08 16:45
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "历史版本详情VO", description = "历史版本详情VO")
public class WorkBookDetailsVO {
    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 文件名称 */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /** 版本号 */
    @ApiModelProperty(value = "版本号")
    private String version;
    /** 版本说明 */
    @ApiModelProperty(value = "版本说明")
    private String versionDescription;
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
    /** 单体类型主键ID */
    @ApiModelProperty(value = "单体类型主键ID")
    private Long productMemberId;
    /** 单体类型名称 */
    @ApiModelProperty(value = "单体类型名称")
    private String memberName;
    /** 业务编码 */
    @ApiModelProperty(value = "业务编码")
    private String businessCode;
    /** 导图集合 */
    @ApiModelProperty(value = "导图集合")
    private List<AttachedFileVO> imageList;
    /** 附件集合 */
    @ApiModelProperty(value = "附件集合")
    private List<AttachedFileVO> annexList;
}
