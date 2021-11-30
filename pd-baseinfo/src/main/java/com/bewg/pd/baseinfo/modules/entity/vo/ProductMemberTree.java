package com.bewg.pd.baseinfo.modules.entity.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品成员树返回对象
 *
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
@Data
@ApiModel(value = "产品成员树返回对象", description = "产品成员树返回对象")
public class ProductMemberTree {
    /** 名称 */
    @ApiModelProperty(value = "产品成员详情")
    private ProductMemberVO productMemberVo;
    /** 名称 */
    @ApiModelProperty(value = "子级产品树集合")
    private List<ProductMemberTree> childProductMemberList;
}
