package com.bewg.pd.baseinfo.modules.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bewg.pd.baseinfo.modules.entity.ProductMember;

/**
 * 产品成员
 *
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
@Mapper
public interface ProductMemberMapper extends BaseMapper<ProductMember> {

    @Select("SELECT parent_id FROM t_product_member WHERE id = (SELECT parent_id FROM t_product_member WHERE id =(SELECT parent_id FROM t_product_member WHERE id =#{productMemberId} ))")
    Long selectProductLineId(@Param("productMemberId") Long productMemberId);
}
