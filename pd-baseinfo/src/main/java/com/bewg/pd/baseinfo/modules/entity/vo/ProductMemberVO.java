package com.bewg.pd.baseinfo.modules.entity.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品成员返回对象
 *
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "产品成员返回对象", description = "产品成员返回对象")
public class ProductMemberVO implements Serializable {

    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String memberName;
    /** 序号 */
    @ApiModelProperty(value = "序号")
    private Integer memberOrder;
    /** 参数类型 */
    @ApiModelProperty(value = "参数类型")
    private String type;
    /** 参数类型 */
    @ApiModelProperty(value = "参数类型名称")
    private String typeName;
    /** 上级主键ID */
    @ApiModelProperty(value = "上级主键ID")
    private Long parentId;
    /** 父级类型 */
    @ApiModelProperty(value = "父级类型")
    private String parentType;
    /** 主键ID */
    @ApiModelProperty(value = "子集数量")
    private Integer childNum;
    /** 主键ID */
    @ApiModelProperty(value = "辅助数据表子集数量")
    private Integer supportChildNum;
    /** 子级类型 */
    @ApiModelProperty(value = "子级类型")
    private String childType;
    /** 子级名称 */
    @ApiModelProperty(value = "子级名称")
    private String childTypeName;
    /** 参数类型 */
    @ApiModelProperty(value = "验证状态")
    private String status;
    /** 业务编码 */
    @ApiModelProperty(value = "业务编码")
    private String businessCode;
    /** icon地址 */
    @ApiModelProperty(value = "icon地址")
    private String iconUrl;
    /** 创建时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /** 创建人员ID */
    @ApiModelProperty(value = "创建人员ID")
    private Long createBy;
    /** 创建人员姓名 */
    @ApiModelProperty(value = "创建人员姓名")
    private String createByUserName;
    /** 最后更新时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;
    /** 最后更新人员ID */
    @ApiModelProperty(value = "最后更新人员ID")
    private Long updateBy;
    /** 最后更新人员姓名 */
    @ApiModelProperty(value = "最后更新人员姓名")
    private String updateByUserName;

}
