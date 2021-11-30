package com.bewg.pd.baseinfo.modules.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bewg.pd.baseinfo.modules.entity.IdsReqParameter;
import com.bewg.pd.baseinfo.modules.entity.TemplateSupportRel;
import com.bewg.pd.baseinfo.modules.service.ITemplateSupportRelService;
import com.bewg.pd.common.aspect.annotation.AutoLog;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.system.query.QueryGenerator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 计算书模板和辅助数据关系表
 *
 * @author dongbd
 * @date 2021-11-09 15:58
 **/
@Slf4j
@Api(tags = "计算书模板和辅助数据关系表")
@RestController
@RequestMapping("/templateSupportRel")
public class TemplateSupportRelController {
    @Autowired
    private ITemplateSupportRelService templateSupportRelService;

    /**
     * 分页列表查询
     *
     * @param templateSupportRel
     *            计算书模板和辅助数据关系表
     * @param pageNo
     *            pageNo
     * @param pageSize
     *            pageSize
     * @param req
     *            req
     * @return Result<IPage<TemplateSupportRel>>
     */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @GetMapping(value = "/list")
    @ApiIgnore
    public Result<IPage<TemplateSupportRel>> queryPageList(TemplateSupportRel templateSupportRel, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
        HttpServletRequest req) {
        Result<IPage<TemplateSupportRel>> result = new Result<>();
        QueryWrapper<TemplateSupportRel> queryWrapper = QueryGenerator.initQueryWrapper(templateSupportRel, req.getParameterMap());
        Page<TemplateSupportRel> page = new Page<>(pageNo, pageSize);
        IPage<TemplateSupportRel> pageList = templateSupportRelService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     * 
     * @param templateSupportRel
     *            templateSupportRel
     * @return Result
     */
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping(value = "/add")
    @ApiIgnore
    public Result<?> add(@RequestBody TemplateSupportRel templateSupportRel) {
        templateSupportRelService.save(templateSupportRel);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     * 
     * @param templateSupportRel
     *            templateSupportRel
     * @return Result
     */
    @AutoLog(value = "编辑")
    @ApiOperation(value = "编辑", notes = "编辑")
    @PutMapping
    @ApiIgnore
    public Result<?> edit(@RequestBody TemplateSupportRel templateSupportRel) {
        Result<?> result = new Result<>();
        TemplateSupportRel templateSupportRelEntity = templateSupportRelService.getById(templateSupportRel.getId());
        if (templateSupportRelEntity == null) {
            result.error500("未找到对应计算书模板和辅助数据关系表");
        } else {
            boolean ok = templateSupportRelService.updateById(templateSupportRel);
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     * 
     * @param id
     *            id
     * @return Result
     */
    @AutoLog(value = "通过id删除")
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @DeleteMapping(value = "/{id}")
    @ApiIgnore
    public Result<?> delete(@PathVariable(name = "id") String id) {
        try {
            templateSupportRelService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败:{}", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     * 
     * @param idsReqParameter
     *            idsReqParameter
     * @return Result<TemplateSupportRel>
     */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @PostMapping(value = "/batch")
    @ApiIgnore
    public Result<TemplateSupportRel> deleteBatch(@RequestBody IdsReqParameter idsReqParameter) {
        Result<TemplateSupportRel> result = new Result<>();
        if (idsReqParameter.getIds().length > 0) {
            this.templateSupportRelService.removeByIds(Arrays.asList(idsReqParameter.getIds()));
            result.setCode(200);
            result.setSuccess(true);
            result.setMessage("删除成功!");
        } else {
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("参数无法识别！");
        }
        return result;
    }

    /**
     * 通过id查询
     * 
     * @param id
     *            id
     * @return Result<TemplateSupportRel>
     */
    @AutoLog(value = "通过id查询")
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping(value = "/{id}}")
    @ApiIgnore
    public Result<TemplateSupportRel> queryById(@PathVariable(name = "id") String id) {
        Result<TemplateSupportRel> result = new Result<>();
        TemplateSupportRel templateSupportRel = templateSupportRelService.getById(id);
        if (templateSupportRel == null) {
            result.error500("未找到对应计算书模板和辅助数据关系表");
        } else {
            result.setResult(templateSupportRel);
            result.setSuccess(true);
        }
        return result;
    }

}
