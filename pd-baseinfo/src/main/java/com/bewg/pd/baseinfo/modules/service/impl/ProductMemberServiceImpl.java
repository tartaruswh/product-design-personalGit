package com.bewg.pd.baseinfo.modules.service.impl;

import static com.bewg.pd.baseinfo.modules.entity.enums.MemberTypeEnum.*;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewg.pd.baseinfo.modules.entity.ProductMember;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook;
import com.bewg.pd.baseinfo.modules.entity.dto.ProductMemberDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.ProductTreeOrderDTO;
import com.bewg.pd.baseinfo.modules.entity.enums.MemberTypeEnum;
import com.bewg.pd.baseinfo.modules.entity.vo.ProductMemberTree;
import com.bewg.pd.baseinfo.modules.entity.vo.ProductMemberVO;
import com.bewg.pd.baseinfo.modules.mapper.ProductMemberMapper;
import com.bewg.pd.baseinfo.modules.mapper.SupportMapper;
import com.bewg.pd.baseinfo.modules.service.IProductMemberService;
import com.bewg.pd.baseinfo.modules.service.ITemplateWorkbookService;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.util.OrikaUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 产品成员
 *
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
@Service
@Slf4j
public class ProductMemberServiceImpl extends ServiceImpl<ProductMemberMapper, ProductMember> implements IProductMemberService {

    @Autowired
    private ProductMemberMapper productMemberMapper;

    @Autowired
    private SupportMapper supportMapper;

    @Autowired
    private ITemplateWorkbookService templateWorkbookService;

    /**
     * 查看产品成员树
     * 
     * @author dongbd
     * @date 2021/10/21 13:14
     * @param id
     *            主键id
     * @param depth
     *            深度
     * @return com.bewg.pd.baseinfo.modules.entity.vo.ProductMemberTree
     */
    @Override
    public Result<ProductMemberTree> queryProductMemberTree(Long id, Integer depth) {
        if (depth <= 0) {
            // 参数depth小于等于0时， 向下查询一层
            depth = 1;
        }
        Result<ProductMemberTree> result = new Result<>();
        ProductMember headProductMember;
        ProductMemberVO headProductMemberVO = new ProductMemberVO();
        ProductMemberTree headProductMemberTree = new ProductMemberTree();

        // 当初始节点不为ROOT时，需查询该Id产品详情
        if (id != 0) {
            headProductMember = productMemberMapper.selectById(id);
            if (headProductMember == null) {
                return result.error500("未找到对应产品成员");
            }
            headProductMemberVO = OrikaUtils.convert(headProductMember, ProductMemberVO.class);
            headProductMemberVO.setParentType(MemberTypeEnum.getParentType(MemberTypeEnum.valueOf(headProductMemberVO.getType())).name());
        } else {
            headProductMemberVO.setId(id);
            headProductMemberVO.setType(ROOT.name());
            headProductMemberVO.setMemberName(ROOT.getDesc());
        }
        MemberTypeEnum memberTypeEnum = MemberTypeEnum.valueOf(headProductMemberVO.getType());
        if (MONOMER_TYPE.getLevel() - memberTypeEnum.getLevel() < depth) {
            // 参数depth过大时， 最多向下查询到单体类型
            depth = MONOMER_TYPE.getLevel() - memberTypeEnum.getLevel();
        }
        headProductMemberVO.setTypeName(memberTypeEnum.getDesc());
        headProductMemberVO.setChildNum(0);

        // 初始节点为单体类型时不需要再向下查询
        if (MONOMER_TYPE == memberTypeEnum) {
            TemplateWorkbook templateWorkbook = templateWorkbookService.selectEnabledVersion(headProductMemberVO.getId());
            if (templateWorkbook != null) {
                headProductMemberVO.setStatus("可用");
            }
            headProductMemberTree.setProductMemberVo(headProductMemberVO);
            result.setResult(headProductMemberTree);
            return result.success("查询产品成员树成功");
        }

        headProductMemberVO.setChildType(MemberTypeEnum.getChildType(memberTypeEnum).name());
        headProductMemberVO.setChildTypeName(MemberTypeEnum.getChildType(memberTypeEnum).getDesc());
        List<ProductMemberTree> childProductMemberTreeList = findChildProductMemberTree(headProductMemberVO, depth, 0);
        headProductMemberTree.setProductMemberVo(headProductMemberVO);
        headProductMemberTree.setChildProductMemberList(childProductMemberTreeList);
        result.setResult(headProductMemberTree);
        return result.success("查询产品成员树成功");
    }

    /**
     * 递归查找子级产品树集合
     * 
     * @author dongbd
     * @date 2021/10/22 09:18
     * @param parentProductMemberVO
     *            父级产品
     * @param depth
     *            深度
     * @param count
     *            计数器(用于阻断递归深度)
     * @return java.util.List<com.bewg.pd.baseinfo.modules.entity.vo.ProductMemberTree>
     */
    private List<ProductMemberTree> findChildProductMemberTree(ProductMemberVO parentProductMemberVO, int depth, int count) {
        if (MemberTypeEnum.valueOf(parentProductMemberVO.getType()) == PRODUCT_LINE) {
            QueryWrapper<Support> queryWrapper = new QueryWrapper<Support>().eq("product_line_id", parentProductMemberVO.getId()).eq("is_del", 0);
            parentProductMemberVO.setSupportChildNum(Math.toIntExact(supportMapper.selectCount(queryWrapper)));
        }
        // 如果已是单体类型, 则不再需要向下查询
        if (MemberTypeEnum.valueOf(parentProductMemberVO.getType()) == MONOMER_TYPE) {
            TemplateWorkbook templateWorkbook = templateWorkbookService.selectEnabledVersion(parentProductMemberVO.getId());
            if (templateWorkbook != null) {
                parentProductMemberVO.setStatus("可用");
            }
            return null;
        }
        List<ProductMemberTree> childProductMemberTreeList = new ArrayList<>();
        // 升序查询所有子级
        QueryWrapper<ProductMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentProductMemberVO.getId()).eq("is_del", 0).orderByAsc("member_order");
        List<ProductMember> childProductMemberList = productMemberMapper.selectList(queryWrapper);
        if (childProductMemberList != null && childProductMemberList.size() > 0) {
            parentProductMemberVO.setChildNum(childProductMemberList.size());

            // 如果已达到查询深度, 则更新完父级对象中的子级数量属性后, 不需要再向下拼接
            if (count >= depth) {
                return null;
            } else {
                count++;
            }
            for (ProductMember childProductMember : childProductMemberList) {
                ProductMemberTree childProductMemberTree = new ProductMemberTree();
                ProductMemberVO childProductMemberVO = OrikaUtils.convert(childProductMember, ProductMemberVO.class);
                MemberTypeEnum memberTypeEnum = MemberTypeEnum.valueOf(childProductMember.getType());
                childProductMemberVO.setTypeName(memberTypeEnum.getDesc());
                childProductMemberVO.setChildNum(0);
                childProductMemberVO.setParentType(parentProductMemberVO.getType());
                if (null != MemberTypeEnum.getChildType(memberTypeEnum)) {
                    childProductMemberVO.setChildType(MemberTypeEnum.getChildType(memberTypeEnum).name());
                    childProductMemberVO.setChildTypeName(MemberTypeEnum.getChildType(memberTypeEnum).getDesc());
                }
                childProductMemberTree.setProductMemberVo(childProductMemberVO);
                List<ProductMemberTree> nextChildProductMemberTreeList = findChildProductMemberTree(childProductMemberVO, depth, count);
                childProductMemberTree.setChildProductMemberList(nextChildProductMemberTreeList);
                childProductMemberTreeList.add(childProductMemberTree);
            }
        }
        return childProductMemberTreeList;
    }

    /**
     * 添加
     * 
     * @author dongbd
     * @date 2021/10/20 17:50
     * @param productMemberDTO
     *            产品成员
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> saveProductMember(ProductMemberDTO productMemberDTO) {
        Result<Long> result = new Result<>();
        ProductMember productMember = new ProductMember();
        productMember.setMemberName(productMemberDTO.getMemberName());
        productMember.setMemberOrder(productMemberDTO.getMemberOrder());
        productMember.setParentId(productMemberDTO.getParentId());
        productMember.setBusinessCode(productMemberDTO.getBusinessCode());
        productMember.setIconUrl(productMemberDTO.getIconUrl());
        List<ProductMember> productMemberList = selectChildList(productMember.getParentId());
        if (CollectionUtils.isNotEmpty(productMemberList)) {
            productMember.setMemberOrder(productMemberList.size() + 1);
        } else {
            productMember.setMemberOrder(1);
        }
        // 根据父级类型添加该级类型
        MemberTypeEnum childType = getChildType(valueOf(productMemberDTO.getParentType()));
        if (null != childType) {
            productMember.setType(childType.name());
        }
        int insert = productMemberMapper.insert(productMember);
        if (insert > 0) {
            result.setSuccess(true);
            result.setMessage("新增成功");
            result.setCode(200);
            result.setResult(productMember.getId());
        } else {
            throw new PdException("新增失败");
        }
        // 校验名称是否重复
        checkMemberName(productMember.getId(), productMemberDTO.getMemberName());
        return result;
    }

    /**
     * 更新
     * 
     * @author dongbd
     * @date 2021/10/21 10:54
     * @param productMemberDTO
     *            产品成员
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateProductMember(ProductMemberDTO productMemberDTO) {
        Result<Object> result = new Result<>();
        ProductMember productMember = super.getById(productMemberDTO.getId());
        if (productMember == null) {
            return result.error500("修改失败: 未找到对应产品成员");
        }
        productMember.setMemberName(productMemberDTO.getMemberName());
        productMember.setMemberOrder(productMemberDTO.getMemberOrder());
        productMember.setBusinessCode(productMemberDTO.getBusinessCode());
        productMember.setIconUrl(productMemberDTO.getIconUrl());
        boolean ok = super.updateById(productMember);
        if (ok) {
            result.success("修改成功");
        }
        // 校验名称是否重复
        checkMemberName(productMemberDTO.getId(), productMemberDTO.getMemberName());
        return result;
    }

    /**
     * 根据id删除产品成员
     * 
     * @author dongbd
     * @date 2021/10/22 09:24
     * @param id
     *            主键id
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteProductMember(Long id) {
        ProductMember parentProductMember = super.getById(id);
        if (parentProductMember == null) {
            throw new PdException("删除失败: 删除的产品不存在");
        }
        // 查询该成员是否有子级
        if (MONOMER_TYPE.name().equals(parentProductMember.getType())) {
            // 单体类型如果有已发布的模板， 不能删除
            int count = templateWorkbookService.deleteByProductMemberId(id);
            if (count > 0) {
                throw new PdException("删除失败: 该单体类型已经包含历史版本, 不能被删除");
            }
        } else {
            List<ProductMember> childProductMemberList = selectChildList(id);
            if (childProductMemberList != null && childProductMemberList.size() > 0) {
                throw new PdException("删除失败: 该【" + MemberTypeEnum.valueOf(parentProductMember.getType()).getDesc() + "】包含【" + MemberTypeEnum.valueOf(childProductMemberList.get(0).getType()).getDesc() + "】, 不能被删除");
            }
        }
        // 删除数据前, 需要将同级且在该成员序号之后的成员序号向前移一位
        List<ProductMember> productMemberList = selectChildList(parentProductMember.getParentId());
        if (CollectionUtils.isNotEmpty(productMemberList)) {
            for (ProductMember productMember : productMemberList) {
                if (productMember.getMemberOrder() >= parentProductMember.getMemberOrder()) {
                    productMember.setMemberOrder(productMember.getMemberOrder() - 1);
                    super.updateById(productMember);
                }
            }
        }

        boolean ok = super.removeById(id);
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 调整产品顺序
     * 
     * @author dongbd
     * @date 2021/10/22 10:26
     * @param productTreeOrderDTO
     *            产品成员
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> editProductTreeOrder(ProductTreeOrderDTO productTreeOrderDTO) {
        HashMap<Long, Integer> orderMap = productTreeOrderDTO.getOrderMap();
        List<ProductMember> childProductMemberList = selectChildList(productTreeOrderDTO.getParentId());
        if (childProductMemberList != null && childProductMemberList.size() > 0) {
            if (childProductMemberList.size() == orderMap.size()) {
                for (ProductMember productMember : childProductMemberList) {
                    if (!orderMap.containsKey(productMember.getId())) {
                        return Result.error("排序失败: 该级产品成员已更新, 请刷新后再排序");
                    }
                }
            } else {
                return Result.error("排序失败: 该级产品数量已更新, 请刷新后再排序");
            }
        }
        Iterator<Map.Entry<Long, Integer>> iterator = orderMap.entrySet().iterator();
        ProductMember productMember = new ProductMember();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();
            productMember.setId(entry.getKey());
            productMember.setMemberOrder(entry.getValue());
            super.updateById(productMember);
        }
        return Result.ok("排序成功");
    }

    /**
     * 根据单体类型Id查产品线Id
     * 
     * @author dongbd
     * @date 2021/11/10 16:14
     * @param productMemberId
     *            单体类型Id
     * @return java.lang.Long
     */
    @Override
    public Long selectProductLineId(Long productMemberId) {
        return productMemberMapper.selectProductLineId(productMemberId);
    }

    /**
     * 查询子级集合
     * 
     * @author dongbd
     * @date 2021/10/22 10:34
     * @param parentId
     *            父级主键id
     * @return java.util.List<com.bewg.pd.baseinfo.modules.entity.ProductMember>
     */
    private List<ProductMember> selectChildList(Long parentId) {
        QueryWrapper<ProductMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId).eq("is_del", 0);
        return productMemberMapper.selectList(queryWrapper);
    }

    /**
     * 校验名称是否重复
     * 
     * @author dongbd
     * @date 2021/10/20 17:51
     * @param id
     *            主键id
     * @param memberName
     *            名称
     */
    void checkMemberName(Long id, String memberName) {
        QueryWrapper<ProductMember> queryWrapper = new QueryWrapper<ProductMember>().eq("member_name", memberName).eq("is_del", 0).ne("id", id);
        Long count = productMemberMapper.selectCount(queryWrapper);
        if (count > 0) {
            log.error("名称重复，请修改:{}", memberName);
            throw new PdException("名称重复，请修改:" + memberName);
        }
    }
}
