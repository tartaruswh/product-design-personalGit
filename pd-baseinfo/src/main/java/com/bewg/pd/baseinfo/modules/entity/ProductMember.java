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
 * 产品成员
 *
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
@Data
@TableName("t_product_member")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "t_product_member对象", description = "产品成员")
public class ProductMember implements Serializable {

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
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
    /** 上级主键ID */
    @ApiModelProperty(value = "上级主键ID")
    private Long parentId;
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
