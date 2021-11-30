package com.bewg.pd.baseinfo.modules.controller;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bewg.pd.baseinfo.modules.entity.IdsReqParameter;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.vo.SupportVo;
import com.bewg.pd.baseinfo.modules.service.ISupportService;
import com.bewg.pd.common.aspect.annotation.AutoLog;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.util.ParamCheckUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 辅助数据表
 * @Author: lizy
 * @Date: 2021-10-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "辅助数据表")
@RestController
@RequestMapping("/support")

public class SupportController {

    @Autowired
    private ISupportService supportService;

    static final int RESULT_CODE = 200;

    /**
     * 编辑
     *
     * @param support
     * @return
     */
    @AutoLog(value = "编辑")
    @ApiOperation(value = "编辑", notes = "编辑")
    @PutMapping(value = "/edit")
    public Result edit(@RequestBody Support support) {
        Result result = new Result<>();
        Support supportEntity = supportService.getById(support.getId());
        if (supportEntity == null) {
            result.error500("未找到对应辅助数据表");
        } else {
            boolean ok = supportService.updateById(support);
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
     * @return
     */
    @AutoLog(value = "通过id删除")
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @DeleteMapping(value = "/delete/{id}")
    public Result<?> delete(@PathVariable(name = "id", required = true) String id) {
        try {
            supportService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param idsReqParameter
     * @return
     */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @PostMapping(value = "/batch")
    public Result<Support> deleteBatch(@RequestBody IdsReqParameter idsReqParameter) {
        Result<Support> result = new Result<>();
        if (idsReqParameter.getIds().length > 0) {
            this.supportService.removeByIds(Arrays.asList(idsReqParameter.getIds()));
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
     * @return
     */
    @AutoLog(value = "通过id查询")
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Support> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<Support> result = new Result<>();
        Support support = supportService.getById(id);
        if (support == null) {
            result.error500("未找到对应辅助数据表");
        } else {
            result.setResult(support);
            result.setSuccess(true);
        }
        return result;
    }

    /***
     * @author Zhaoyubo
     * @description 新增辅助数据表
     * @date 2021/10/21
     * @param file:
     * @param id:
     * @param productLineId:
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */

    @AutoLog(value = "新增辅助数据表")
    @ApiOperation(value = "新增辅助数据表", notes = "新增辅助数据表")
    @PostMapping(value = "/datesheet")
    public Result supportAdd(@RequestParam(name = "file", required = true) MultipartFile file, @RequestParam(name = "id", required = false) Long id,
        @RequestParam(name = "productLineId", required = true, defaultValue = "0") Long productLineId) {
        if (productLineId == 0) {
            throw new PdException("产品线不存在");
        }
        if (file == null) {
            throw new PdException("上传文件为空");
        }
        if (id == null) {
            // 执行新增
            return supportService.supportAdd(file, productLineId);
        } else {
            // 执行更新
            return supportService.supportUpdate(file, id, productLineId);
        }
    }

    /***
     * @author Zhaoyubo
     * @description 下载辅助数据表
     * @date 2021/10/21
     * @param id:
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result
     */
    @AutoLog(value = "下载辅助数据表")
    @ApiOperation(value = "下载辅助数据表", notes = "下载辅助数据表")
    @GetMapping(value = "/datasheet/{id}")
    public Result download(@PathVariable(name = "id", required = true) Long id, HttpServletResponse response) throws UnsupportedEncodingException {
        String excelId = id.toString();
        Result<Support> supportResult = queryById(excelId);
        Support support = supportResult.getResult();
        if (support == null) {
            throw new PdException("该文件不存在");
        }
        String excelName = support.getExcelName();
        String fileExtension = support.getFileExtension();
        String excelUrl = support.getExcelUrl();
        Result result = supportService.downloadSheet(excelName, excelUrl, fileExtension, response);
        if (result.getCode() == RESULT_CODE) {
            return result;
        } else {
            throw new PdException("下载失败");
        }
    }

    /***
     * @author Zhaoyubo
     * @description 删除辅助数据表
     * @date 2021/10/20
     * @param id:
     * @return com.bewg.pd.baseinfo.modules.common.vo.Result<com.bewg.pd.baseinfo.modules.entity.Support>
     */
    @AutoLog(value = "删除辅助数据表")
    @ApiOperation(value = "删除辅助数据表", notes = "删除辅助数据表")
    @DeleteMapping(value = "/datasheet/{id}")
    public Result supportDelete(@PathVariable(name = "id", required = true) Long id) {
        Result result = new Result();
        if (id == null || "".equals(id)) {
            return result.error500("参数不识别！");
        } else {
            Result resultMessage = supportService.deleteSheet(id);
            return resultMessage;
        }

    }

    /***
     * @author Zhaoyubo
     * @description 展示辅助数据表
     * @date 2021/10/20
     * @param productLineId:
     * @return com.bewg.pd.baseinfo.modules.entity.Support
     */
    @AutoLog(value = "展示辅助数据表")
    @ApiOperation(value = "展示辅助数据表", notes = "展示辅助数据表")
    @GetMapping(value = "/datasheetTree")
    public Result<List<SupportVo>> supportTree(@RequestParam(name = "productLineId", defaultValue = "0") Long productLineId) {
        if (productLineId == 0) {
            throw new PdException("产品线不存在");
        }
        Result<List<SupportVo>> result = supportService.findSupport(productLineId);
        return result;
    }

    /**
     * 上传辅助数据表(新增计算书版本页面接口)
     * 
     * @author dongbd
     * @date 2021/11/16 10:39
     * @param file
     *            辅助数据表excel
     * @param targetName
     *            目标名称
     * @param productMemberId
     *            单体类型Id
     * @return com.bewg.pd.common.entity.vo.Result<java.lang.Boolean>
     */
    @AutoLog(value = "上传辅助数据表(新增计算书版本页面接口)")
    @ApiOperation(value = "上传辅助数据表(新增计算书版本页面接口)", notes = "上传辅助数据表(新增计算书版本页面接口)")
    @PostMapping(value = "/supportExcel")
    public Result<?> uploadSupport(@ApiParam(value = "辅助数据表excel") @RequestParam(name = "file") MultipartFile file, @ApiParam(value = "目标名称") @RequestParam(name = "targetName") String targetName,
        @ApiParam(value = "单体类型Id") @RequestParam(name = "productMemberId", defaultValue = "0") Long productMemberId) {
        ParamCheckUtil.checkEmpty("名称", targetName);
        ParamCheckUtil.checkEmpty("单体类型Id", productMemberId);
        ParamCheckUtil.checkEmptyFile(file);
        return supportService.uploadSupport(file, targetName, productMemberId);
    }
}