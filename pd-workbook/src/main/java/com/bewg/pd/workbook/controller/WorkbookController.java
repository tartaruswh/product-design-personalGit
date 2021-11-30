package com.bewg.pd.workbook.controller;

import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.entity.req.WorkbookCalcReq;
import com.bewg.pd.common.entity.req.WorkbookContextReq;
import com.bewg.pd.common.entity.req.WorkbookFileReq;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.workbook.service.IWorkbookService;
import com.bewg.pd.workbook.utils.CommonUtils;
import com.bewg.pd.workbook.utils.FastdfsUtil;
import com.grapecity.documents.excel.Workbook;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * <p>
 * 计算书模板(控制器)
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Slf4j
@RequestMapping("/workbook")
@RestController
@Api(tags = "计算书模板")
public class WorkbookController {

    @Autowired
    @Qualifier(value = "gcWorkbookServiceImpl")
    private IWorkbookService workbookService;

    /**
     * 上传文件(仅供测试使用)
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传文件(仅测试)", notes = "上传文件(仅测试)")
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        if (file == null) {
            return Result.error("文件为空!");
        }
        return Result.ok(CommonUtils.upload(file, CommonConstant.UPLOAD_TYPE_FASTDFS));
    }

    /**
     * 解析计算书文件
     *
     * @param workbookFileReq
     * @return
     */
    @ApiOperation(value = "解析计算书", notes = "解析计算书")
    @PostMapping("/parse")
    public Result parse(@RequestBody @Valid WorkbookFileReq workbookFileReq) {
        Workbook workbook = new Workbook();
        workbook.open(FastdfsUtil.getFileInputStream(workbookFileReq.getFilePath()));
        return workbookService.parseExcel(workbook);
    }

    /**
     * 获取计算书外部引用文件列表
     *
     * @param workbookFileReq
     * @return
     */
    @ApiOperation(value = "解析外部引用文件", notes = "解析计算书")
    @PostMapping("/refs/parse")
    public Result getRefFileNames(@RequestBody @Valid WorkbookFileReq workbookFileReq) {
        return workbookService.getRefFileNames(FastdfsUtil.getFileInputStream(workbookFileReq.getFilePath()));
    }

    /**
     * 驱动计算
     *
     * @param workbookCalcReq
     * @return
     */
    @ApiOperation(value = "驱动计算", notes = "更改入参驱动计算")
    @PostMapping("/calculate")
    public Result calculate(@RequestBody @Valid WorkbookCalcReq workbookCalcReq) {
        return workbookService.calculate(workbookCalcReq);
    }

    /**
     * 创建计算书上下文实例
     *
     * @param workbookContextReq
     * @return
     */
    @ApiOperation(value = "创建上下文", notes = "创建上下文")
    @PostMapping("/context/create")
    public Result createContext(@RequestBody @Valid WorkbookContextReq workbookContextReq) {
        return workbookService.createContext(workbookContextReq);
    }

}