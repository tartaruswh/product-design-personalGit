package com.bewg.pd.baseinfo.modules.entity.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品成员参数
 *
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "产品成员参数", description = "产品成员参数")
public class ProductMemberDTO implements Serializable {

    /** 主键ID */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /** 名称 */
    @ApiModelProperty(value = "名称")
    private String memberName;
    /** 序号 */
    @ApiModelProperty(value = "序号")
    private Integer memberOrder;
    /** 父级参数类型 */
    @ApiModelProperty(value = "父级参数类型")
    private String parentType;
    /** 上级主键ID */
    @ApiModelProperty(value = "上级主键ID")
    private Long parentId;
    /** 业务编码 */
    @ApiModelProperty(value = "业务编码")
    private String businessCode;
    /** icon地址 */
    @ApiModelProperty(value = "icon地址")
    private String iconUrl;
}
