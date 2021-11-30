package com.bewg.pd.baseinfo.modules.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bewg.pd.baseinfo.modules.entity.AttachedFile;
import com.bewg.pd.baseinfo.modules.entity.IdsReqParameter;
import com.bewg.pd.baseinfo.modules.entity.dto.AttachedFileDTO;
import com.bewg.pd.baseinfo.modules.service.IAttachedFileService;
import com.bewg.pd.common.aspect.annotation.AutoLog;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.system.query.QueryGenerator;
import com.bewg.pd.common.util.ParamCheckUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 模板附属文件表
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
@Slf4j
@Api(tags = "模板附属文件表")
@RestController
@RequestMapping("/attachedFile")
public class AttachedFileController {
    @Autowired
    private IAttachedFileService attachedFileService;

    /**
     * 分页列表查询
     * 
     * @param attachedFile
     *            attachedFile
     * @param pageNo
     *            pageNo
     * @param pageSize
     *            pageSize
     * @param req
     *            req
     * @return Result<IPage<AttachedFile>>
     */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @GetMapping(value = "/list")
    @ApiIgnore
    public Result<IPage<AttachedFile>> queryPageList(AttachedFile attachedFile, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
        HttpServletRequest req) {
        Result<IPage<AttachedFile>> result = new Result<>();
        QueryWrapper<AttachedFile> queryWrapper = QueryGenerator.initQueryWrapper(attachedFile, req.getParameterMap());
        Page<AttachedFile> page = new Page<>(pageNo, pageSize);
        IPage<AttachedFile> pageList = attachedFileService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     * 
     * @param attachedFile
     *            attachedFile
     * @return Result
     */
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping(value = "/add")
    @ApiIgnore
    public Result<?> add(@RequestBody AttachedFile attachedFile) {
        attachedFileService.save(attachedFile);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     * 
     * @param attachedFile
     *            attachedFile
     * @return Result
     */
    @AutoLog(value = "编辑")
    @ApiOperation(value = "编辑", notes = "编辑")
    @PutMapping
    @ApiIgnore
    public Result<?> edit(@RequestBody AttachedFile attachedFile) {
        Result<?> result = new Result<>();
        AttachedFile attachedFileEntity = attachedFileService.getById(attachedFile.getId());
        if (attachedFileEntity == null) {
            result.error500("未找到对应模板附属文件表");
        } else {
            boolean ok = attachedFileService.updateById(attachedFile);
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
            attachedFileService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败:{}", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 通过id删除附件
     * 
     * @author dongbd
     * @date 2021/11/22 09:34
     * @param id
     *            主键id
     * @return com.bewg.pd.common.entity.vo.Result<?>
     */
    @AutoLog(value = "通过id删除附件")
    @ApiOperation(value = "通过id删除附件", notes = "通过id删除附件")
    @DeleteMapping(value = "/annex/{id}")
    public Result<?> removeAnnex(@PathVariable(name = "id") Long id) {
        ParamCheckUtil.checkEmpty("主键ID", id);
        try {
            attachedFileService.removeAnnex(id);
        } catch (Exception e) {
            log.error("删除附件失败:{}", e.getMessage());
            return Result.error("删除附件失败");
        }
        return Result.ok("删除附件成功");
    }

    /**
     * 批量删除
     * 
     * @param idsReqParameter
     *            idsReqParameter
     * @return Result<AttachedFile>
     */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @PostMapping(value = "/batch")
    @ApiIgnore
    public Result<AttachedFile> deleteBatch(@RequestBody IdsReqParameter idsReqParameter) {
        Result<AttachedFile> result = new Result<>();
        if (idsReqParameter.getIds().length > 0) {
            this.attachedFileService.removeByIds(Arrays.asList(idsReqParameter.getIds()));
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
     * @return Result<AttachedFile>
     */
    @AutoLog(value = "通过id查询")
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping(value = "/{id}}")
    @ApiIgnore
    public Result<AttachedFile> queryById(@PathVariable(name = "id") String id) {
        Result<AttachedFile> result = new Result<>();
        AttachedFile attachedFile = attachedFileService.getById(id);
        if (attachedFile == null) {
            result.error500("未找到对应模板附属文件表");
        } else {
            result.setResult(attachedFile);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 上传导图
     * 
     * @author dongbd
     * @date 2021/11/11 12:24
     * @param files
     *            导图集合
     * @param attachedFileDTO
     *            上传导图所需参数
     * @return com.bewg.pd.common.entity.vo.Result<?>
     */
    @AutoLog(value = "上传导图")
    @ApiOperation(value = "上传导图", notes = "上传导图")
    @PostMapping(value = "/image")
    @ApiIgnore
    public Result<?> upLoadImage(@ApiParam(value = "导图集合") MultipartFile[] files, @ApiParam(value = "上传导图所需参数") AttachedFileDTO attachedFileDTO) {
        ParamCheckUtil.checkEmptyFiles(files);
        ParamCheckUtil.checkEmpty("计算书模板Id", attachedFileDTO.getTemplateWorkbookId());
        return attachedFileService.upLoadImage(files, attachedFileDTO, null);
    }

    /**
     * 上传附件
     * 
     * @author dongbd
     * @date 2021/11/11 12:24
     * @param files
     *            附件集合
     * @param attachedFileDTO
     *            上传附件所需参数
     * @return com.bewg.pd.common.entity.vo.Result<?>
     */
    @AutoLog(value = "上传附件")
    @ApiOperation(value = "上传附件", notes = "上传附件")
    @PostMapping(value = "/annex")
    public Result<Long> upLoadAnnex(@ApiParam(value = "附件") MultipartFile[] files, @ApiParam(value = "上传附件所需参数") AttachedFileDTO attachedFileDTO) {
        ParamCheckUtil.checkEmptyFiles(files);
        ParamCheckUtil.checkEmpty("单体类型ID", attachedFileDTO.getProductMemberId());
        ParamCheckUtil.checkEmpty("创建人ID", attachedFileDTO.getCreateBy());
        return attachedFileService.upLoadAnnex(files, attachedFileDTO);
    }
}
