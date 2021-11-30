package com.bewg.pd.baseinfo.modules.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bewg.pd.baseinfo.modules.entity.IdsReqParameter;
import com.bewg.pd.baseinfo.modules.entity.ProductMember;
import com.bewg.pd.baseinfo.modules.entity.dto.ProductMemberDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.ProductTreeOrderDTO;
import com.bewg.pd.baseinfo.modules.entity.vo.ProductMemberTree;
import com.bewg.pd.baseinfo.modules.service.IProductMemberService;
import com.bewg.pd.common.aspect.annotation.AutoLog;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.system.query.QueryGenerator;
import com.bewg.pd.common.util.ParamCheckUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 产品成员
 * 
 * @author dongbd
 * @date 2021/10/24 08:23
 **/
@Slf4j
@Api(tags = "产品成员")
@RestController
@RequestMapping("/productMember")
public class ProductMemberController {

    @Autowired
    private IProductMemberService productMemberService;

    /**
     * 分页列表查询
     * 
     * @param productMember
     *            产品成员
     * @param pageNo
     *            pageNo
     * @param pageSize
     *            pageSize
     * @param req
     *            req
     * @return Result<IPage<ProductMember>>
     */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @GetMapping(value = "/list")
    @ApiIgnore
    public Result<IPage<ProductMember>> queryPageList(ProductMember productMember, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
        HttpServletRequest req) {
        Result<IPage<ProductMember>> result = new Result<>();
        QueryWrapper<ProductMember> queryWrapper = QueryGenerator.initQueryWrapper(productMember, req.getParameterMap());
        Page<ProductMember> page = new Page<>(pageNo, pageSize);
        IPage<ProductMember> pageList = productMemberService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     * 
     * @param productMemberDTO
     *            产品成员
     * @return Result
     */
    @AutoLog(value = "(已发布)添加")
    @ApiOperation(value = "(已发布)添加", notes = "(已发布)添加")
    @PostMapping
    public Result<Long> add(@RequestBody ProductMemberDTO productMemberDTO) {
        ParamCheckUtil.checkEmpty("名称", productMemberDTO.getMemberName());
        ParamCheckUtil.checkEmptyWithZero("父级ID", productMemberDTO.getParentId());
        ParamCheckUtil.checkEmpty("父级类型", productMemberDTO.getParentType());
        return productMemberService.saveProductMember(productMemberDTO);
    }

    /**
     * 编辑
     * 
     * 
     * @param productMemberDTO
     *            产品成员
     * @return Result
     */
    @AutoLog(value = "(已发布)编辑")
    @ApiOperation(value = "(已发布)编辑", notes = "(已发布)编辑")
    @PutMapping
    public Result<?> edit(@RequestBody ProductMemberDTO productMemberDTO) {
        ParamCheckUtil.checkEmpty("主键ID", productMemberDTO.getId());
        ParamCheckUtil.checkEmpty("名称", productMemberDTO.getMemberName());
        return productMemberService.updateProductMember(productMemberDTO);
    }

    /**
     * 通过id删除
     * 
     * @param id
     *            主键id
     * @return Result
     */
    @AutoLog(value = "(已发布)通过id删除")
    @ApiOperation(value = "(已发布)通过id删除", notes = "(已发布)通过id删除")
    @DeleteMapping(value = "/{id}")
    public Result<?> delete(@PathVariable(name = "id") Long id) {
        ParamCheckUtil.checkEmpty("主键ID", id);
        return productMemberService.deleteProductMember(id);
    }

    /**
     * 批量删除
     * 
     * @param idsReqParameter
     *            主键集合
     * @return Result<ProductMember>
     */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @PostMapping(value = "/batch")
    @ApiIgnore
    public Result<?> deleteBatch(@RequestBody IdsReqParameter idsReqParameter) {
        Result<?> result = new Result<>();
        if (idsReqParameter.getIds().length > 0) {
            productMemberService.removeByIds(Arrays.asList(idsReqParameter.getIds()));
            result.setCode(CommonConstant.SC_OK_200);
            result.setSuccess(true);
            result.setMessage("删除成功!");
        } else {
            result.setCode(CommonConstant.SC_INTERNAL_SERVER_ERROR_500);
            result.setSuccess(false);
            result.setMessage("参数无法识别！");
        }
        return result;
    }

    /**
     * 通过id查询
     * 
     * @param id
     *            主键id
     * @return Result<ProductMember>
     */
    @AutoLog(value = "(已发布)通过id查询")
    @ApiOperation(value = "(已发布)通过id查询", notes = "(已发布)通过id查询")
    @GetMapping(value = "/productMember")
    public Result<ProductMember> queryById(@RequestParam(name = "id") String id) {
        ParamCheckUtil.checkEmpty("主键ID", id);
        Result<ProductMember> result = new Result<>();
        ProductMember productMember = productMemberService.getById(id);
        if (productMember == null) {
            result.setCode(CommonConstant.SC_INTERNAL_SERVER_ERROR_500);
            result.setSuccess(false);
            result.setMessage("未找到对应产品成员");
        } else {
            result.setCode(CommonConstant.SC_OK_200);
            result.setSuccess(true);
            result.setMessage("查询成功!");
            result.setResult(productMember);
        }
        return result;
    }

    /**
     * 查看产品成员树
     * 
     * @param id
     *            主键id
     * @return Result<ProductMemberTree>
     */
    @AutoLog(value = "(已发布)查看产品成员树")
    @ApiOperation(value = "(已发布)查看产品成员树", notes = "(已发布)查看产品成员树")
    @GetMapping(value = "/productMemberTree")
    public Result<ProductMemberTree> queryProductMemberTree(@ApiParam(value = "主键id") @RequestParam(name = "id") Long id, @ApiParam(value = "层数") @RequestParam(name = "depth", required = false, defaultValue = "1") Integer depth) {
        ParamCheckUtil.checkEmptyWithZero("主键ID", id);
        return productMemberService.queryProductMemberTree(id, depth);
    }

    /**
     * 调整产品成员顺序
     * 
     * @param productTreeOrderDTO
     *            产品成员新排序
     * @return Result
     */
    @AutoLog(value = "(已发布)调整产品成员顺序")
    @ApiOperation(value = "(已发布)调整产品成员顺序", notes = "(已发布)调整产品成员顺序")
    @PostMapping(value = "/productTreeOrder")
    public Result<?> productTreeOrder(@RequestBody ProductTreeOrderDTO productTreeOrderDTO) {
        ParamCheckUtil.checkEmptyWithZero("父级ID", productTreeOrderDTO.getParentId());
        ParamCheckUtil.checkEmpty("子级集合", productTreeOrderDTO.getOrderMap());
        return productMemberService.editProductTreeOrder(productTreeOrderDTO);
    }
}
