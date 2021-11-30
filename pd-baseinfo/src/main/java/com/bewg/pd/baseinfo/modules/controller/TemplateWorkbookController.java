package com.bewg.pd.baseinfo.modules.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bewg.pd.baseinfo.modules.entity.IdsReqParameter;
import com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook;
import com.bewg.pd.baseinfo.modules.entity.dto.ReleaseTemplateDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.RemoveTemplateDTO;
import com.bewg.pd.baseinfo.modules.entity.vo.SupportValidateVO;
import com.bewg.pd.baseinfo.modules.entity.vo.TemplateWorkbookVO;
import com.bewg.pd.baseinfo.modules.entity.vo.WorkBookDetailsVO;
import com.bewg.pd.baseinfo.modules.entity.vo.WorkBookParseVO;
import com.bewg.pd.baseinfo.modules.service.ITemplateWorkbookService;
import com.bewg.pd.common.aspect.annotation.AutoLog;
import com.bewg.pd.common.entity.excel.Sheet;
import com.bewg.pd.common.entity.req.WorkbookCalcReq;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.system.query.QueryGenerator;
import com.bewg.pd.common.util.ParamCheckUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description: 计算书模板
 * @Author: Zhaoyubo
 * @Date: 2021-11-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "计算书模板")
@RestController
@RequestMapping("/templateworkbook")
public class TemplateWorkbookController {
    @Autowired
    private ITemplateWorkbookService templateWorkbookService;

    /**
     * 分页列表查询
     * 
     * @param templateWorkbook
     *            计算书实体类
     * @param pageNo
     *            展示第几页
     * @param pageSize
     *            每页展示多少
     * @param req
     *            传输数据
     * @return Result<IPage<TemplateWorkbook>>
     */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @GetMapping(value = "/list")
    @ApiIgnore
    public Result<IPage<TemplateWorkbook>> queryPageList(TemplateWorkbook templateWorkbook, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
        HttpServletRequest req) {
        Result<IPage<TemplateWorkbook>> result = new Result<>();
        QueryWrapper<TemplateWorkbook> queryWrapper = QueryGenerator.initQueryWrapper(templateWorkbook, req.getParameterMap());
        Page<TemplateWorkbook> page = new Page<>(pageNo, pageSize);
        IPage<TemplateWorkbook> pageList = templateWorkbookService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     * 
     * @param templateWorkbook
     *            计算书实体类
     * @return Result
     */
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping(value = "/add")
    @ApiIgnore
    public Result<?> add(@RequestBody TemplateWorkbook templateWorkbook) {
        templateWorkbookService.save(templateWorkbook);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     * 
     * @param templateWorkbook
     *            计算书模板
     * @return Result
     */
    @AutoLog(value = "编辑")
    @ApiOperation(value = "编辑", notes = "编辑")
    @PutMapping
    public Result<Object> edit(@RequestBody TemplateWorkbook templateWorkbook) {
        ParamCheckUtil.checkEmpty("计算书模板ID", templateWorkbook.getId());
        Result<Object> result = new Result<>();
        TemplateWorkbook templateWorkbookEntity = templateWorkbookService.getById(templateWorkbook.getId());
        if (templateWorkbookEntity == null) {
            result.error500("未找到对应计算书模板");
        } else {
            boolean ok = templateWorkbookService.updateById(templateWorkbook);
            if (ok) {
                result.success("修改成功");
            }
        }
        return result;
    }

    /**
     * 批量删除
     * 
     * @param idsReqParameter
     *            idsReqParameter
     * @return Result<TemplateWorkbook>
     */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @PostMapping(value = "/batch")
    @ApiIgnore
    public Result<TemplateWorkbook> deleteBatch(@RequestBody IdsReqParameter idsReqParameter) {
        Result<TemplateWorkbook> result = new Result<>();
        if (idsReqParameter.getIds().length > 0) {
            this.templateWorkbookService.removeByIds(Arrays.asList(idsReqParameter.getIds()));
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
     * 通过id查询历史版本详情
     * 
     * @param id
     *            计算书模板Id
     * @return Result<TemplateWorkbook>
     */
    @AutoLog(value = "通过id查询")
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping(value = "/{id}")
    public Result<WorkBookDetailsVO> queryById(@PathVariable(name = "id") Long id) {
        ParamCheckUtil.checkEmpty("计算书模板ID", id);
        return templateWorkbookService.getDetailsById(id);
    }

    /**
     * 清理未发布的垃圾文件(用于跳转新增编辑页面及返回按钮)
     * 
     * @author dongbd
     * @date 2021/11/19 15:53
     * @param removeTemplateDTO
     * @return com.bewg.pd.common.entity.vo.Result<?>
     */
    @AutoLog(value = "清理未发布的垃圾文件(用于跳转新增编辑页面及返回按钮)")
    @ApiOperation(value = "清理未发布的垃圾文件(用于跳转新增编辑页面及返回按钮)", notes = "清理未发布的垃圾文件(用于跳转新增编辑页面及返回按钮)")
    @PostMapping(value = "/NoReleaseFile")
    public Result<?> cleanGarbageFiles(@RequestBody RemoveTemplateDTO removeTemplateDTO) {
        ParamCheckUtil.checkEmpty("操作类型", removeTemplateDTO.getBtnType());
        ParamCheckUtil.checkEmpty("单体类型ID", removeTemplateDTO.getProductMemberId());
        ParamCheckUtil.checkEmpty("创建人ID", removeTemplateDTO.getCreateBy());
        return templateWorkbookService.cleanGarbageFiles(removeTemplateDTO);
    }

    /**
     * 上传计算书模板
     *
     * @author dongbd
     * @date 2021/11/05 13:34
     * @param file
     *            计算书模板excel
     * @param productMemberId
     *            单体类型Id
     * @param createBy
     *            创建人Id
     * @return com.bewg.pd.common.entity.vo.Result<java.lang.Long>
     */
    @AutoLog(value = "上传计算书模板")
    @ApiOperation(value = "上传计算书模板", notes = "上传计算书模板")
    @PostMapping(value = "/excel")
    public Result<Long> upLoadExcel(@ApiParam(value = "计算书模板excel") @RequestParam(name = "file") MultipartFile file, @ApiParam(value = "单体类型Id") @RequestParam(name = "productMemberId", defaultValue = "0") Long productMemberId,
        @ApiParam(value = "创建人Id") @RequestParam(name = "createBy", defaultValue = "0") Long createBy) {
        ParamCheckUtil.checkEmpty("单体类型ID", productMemberId);
        ParamCheckUtil.checkEmpty("创建人ID", createBy);
        ParamCheckUtil.checkEmptyFile(file);
        return templateWorkbookService.upLoadExcel(file, productMemberId, createBy);
    }

    /**
     * 验证计算书模板
     *
     * @author dongbd
     * @date 2021/11/08 9:57
     * @param id
     *            计算书模板id
     * @return Result<SupportValidateVO>
     */
    @AutoLog(value = "验证计算书模板")
    @ApiOperation(value = "验证计算书模板", notes = "验证计算书模板")
    @GetMapping(value = "/validateResult")
    public Result<SupportValidateVO> validateExcel(@ApiParam(value = "计算书模板id") @RequestParam(name = "id", defaultValue = "0") Long id) {
        ParamCheckUtil.checkEmpty("计算书模板主键ID", id);
        return templateWorkbookService.validateExcel(id);
    }

    /**
     * 验证计算书模板并生成上下文
     *
     * @author dongbd
     * @date 2021/11/05 13:22
     * @param id
     *            计算书模板id
     * @param contextId
     *            上下文id
     * @return com.bewg.pd.common.entity.vo.Result<String>
     */
    @AutoLog(value = "验证计算书模板并生成上下文")
    @ApiOperation(value = "验证计算书模板并生成上下文", notes = "验证计算书模板并生成上下文")
    @GetMapping(value = "/validateResult/context")
    public Result<?> validateAndCreatContext(@ApiParam(value = "计算书模板id") @RequestParam(name = "id", defaultValue = "0") Long id, @ApiParam(value = "上下文编号") @RequestParam(name = "contextId", required = false) String contextId) {
        ParamCheckUtil.checkEmpty("计算书模板主键ID", id);
        return templateWorkbookService.validateAndCreatContext(id, contextId);
    }

    /**
     * 解析计算书模板Excel
     *
     * @author dongbd
     * @date 2021/11/05 13:22
     * @param id
     *            计算书模板id
     * @return com.bewg.pd.common.entity.vo.Result<WorkBookParseVO>
     */
    @AutoLog(value = "解析计算书模板")
    @ApiOperation(value = "解析计算书模板", notes = "解析计算书模板")
    @GetMapping(value = "/parseResult")
    public Result<WorkBookParseVO> parseExcel(@ApiParam(value = "计算书模板id") @RequestParam(name = "id", defaultValue = "0") Long id) {
        ParamCheckUtil.checkEmpty("计算书模板主键ID", id);
        return templateWorkbookService.parseExcel(id);
    }

    /**
     * 创建上下文
     *
     * @author dongbd
     * @date 2021/11/05 13:22
     * @param id
     *            计算书模板id
     * @return Result<String>
     */
    @AutoLog(value = "创建上下文")
    @ApiOperation(value = "创建上下文", notes = "创建上下文")
    @GetMapping(value = "/context")
    public Result<String> creatContext(@ApiParam(value = "计算书模板id") @RequestParam(name = "id", defaultValue = "0") Long id) {
        ParamCheckUtil.checkEmpty("计算书模板主键ID", id);
        return templateWorkbookService.creatContext(id);
    }

    /**
     * 驱动计算计算书模板
     *
     * @author dongbd
     * @date 2021/11/05 13:22
     * @param workbookCalcReq
     *            workbookCalcReq
     * @return com.bewg.pd.common.entity.vo.Result<com.bewg.pd.common.entity.excel.Sheet>
     */
    @AutoLog(value = "驱动计算计算书模板")
    @ApiOperation(value = "驱动计算计算书模板", notes = "驱动计算计算书模板")
    @PostMapping(value = "/calculationResult")
    public Result<Sheet> calculationResult(@RequestBody WorkbookCalcReq workbookCalcReq) {
        ParamCheckUtil.checkEmpty("上下文编号", workbookCalcReq.getContextId());
        return templateWorkbookService.calculateExcel(workbookCalcReq);
    }

    /**
     * 发布版本
     *
     * @param files
     *            导图集合
     * @param releaseTemplateDTO
     *            模板发布所需参数
     * @return Result
     * @author dongbd
     * @date 2021/11/05 13:22
     */
    @AutoLog(value = "发布版本")
    @ApiOperation(value = "发布版本", notes = "发布版本")
    @PostMapping(value = "/excelVersion")
    public Result<Long> releaseExcel(@ApiParam(value = "导图集合") MultipartFile[] files, @ApiParam(value = "计算书模板id") ReleaseTemplateDTO releaseTemplateDTO) {
        // ParamCheckUtil.checkEmptyFiles(files);
        ParamCheckUtil.checkEmpty("计算书模板主键ID", releaseTemplateDTO.getId());
        ParamCheckUtil.checkEmpty("版本说明", releaseTemplateDTO.getVersionDescription());
        ParamCheckUtil.checkEmpty("创建人ID", releaseTemplateDTO.getCreateBy());
        return templateWorkbookService.releaseExcel(files, releaseTemplateDTO);
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
    public Result<?> delete(@ApiParam(value = "计算书模板ID") @PathVariable(name = "id") Long id) {
        ParamCheckUtil.checkEmpty("计算书模板ID", id);
        return templateWorkbookService.removeWorkFile(id);
    }

    /**
     * 设置计算书可用状态
     * 
     * @author Zhaoyubo
     * @date 2021/11/03 15:24
     * @param id
     *            主键ID
     * @param candidate
     *            可用状态
     * @return com.bewg.pd.common.entity.vo.Result
     */
    @AutoLog(value = "设置计算书可用状态")
    @ApiOperation(value = "设置计算书可用状态", notes = "设置计算书可用状态")
    @GetMapping(value = "/isAvailable/{id}/{candidate}")
    public Result<Object> isAvailable(@PathVariable(name = "id", required = true) Long id, @PathVariable(name = "candidate", required = true) Integer candidate) {
        Result<Object> result = templateWorkbookService.isAvailable(id, candidate);
        return result.success("状态改变成功");
    }

    /**
     * 查看计算书版本列表
     *
     * @author Zhaoyubo
     * @date 2021/11/04 10:22
     * @param productMemberId
     *            单体类型主键Id
     * @return com.bewg.pd.common.entity.vo.Result
     */
    @AutoLog(value = "查看计算书版本列表")
    @ApiOperation(value = "查看计算书版本列表", notes = "查看计算书版本列表")
    @GetMapping(value = "/queryTemplateWorkbook/{productMemberId}")
    public Result<List<TemplateWorkbookVO>> queryTemplateWorkbook(@PathVariable(name = "productMemberId", required = true) Long productMemberId) {
        return templateWorkbookService.queryTemplateWorkbook(productMemberId);
    }

}
