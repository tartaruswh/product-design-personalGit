package com.bewg.pd.baseinfo.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bewg.pd.baseinfo.modules.entity.ProductMember;
import com.bewg.pd.baseinfo.modules.entity.dto.ProductMemberDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.ProductTreeOrderDTO;
import com.bewg.pd.baseinfo.modules.entity.vo.ProductMemberTree;
import com.bewg.pd.common.entity.vo.Result;

/**
 * 产品成员
 *
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
public interface IProductMemberService extends IService<ProductMember> {
    /**
     * 查看产品成员树
     * 
     * @author dongbd
     * @date 2021/10/22 13:17
     * @param id
     *            主键id
     * @param depth
     *            深度
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result<com.bewg.pd.baseinfo.modules.entity.vo.ProductMemberTree>
     */
    Result<ProductMemberTree> queryProductMemberTree(Long id, Integer depth);

    /**
     * 添加
     * 
     * @author dongbd
     * @date 2021/10/22 13:19
     * @param productMemberDTO
     *            产品成员
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    Result<Long> saveProductMember(ProductMemberDTO productMemberDTO);

    /**
     * 更新
     * 
     * @author dongbd
     * @date 2021/10/22 13:19
     * @param productMemberDTO
     *            产品成员
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    Result<?> updateProductMember(ProductMemberDTO productMemberDTO);

    /**
     * 根据id删除产品成员
     * 
     * @author dongbd
     * @date 2021/10/22 13:19
     * @param id
     *            主键id
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    Result<?> deleteProductMember(Long id);

    /**
     * 调整产品顺序
     * 
     * @author dongbd
     * @date 2021/10/22 13:19
     * @param productTreeOrderDTO
     *            产品成员
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    Result<?> editProductTreeOrder(ProductTreeOrderDTO productTreeOrderDTO);

    Long selectProductLineId(Long productMemberId);
}
