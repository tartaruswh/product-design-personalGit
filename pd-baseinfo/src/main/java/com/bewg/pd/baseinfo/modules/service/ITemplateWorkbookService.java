package com.bewg.pd.baseinfo.modules.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook;
import com.bewg.pd.baseinfo.modules.entity.dto.ReleaseTemplateDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.RemoveTemplateDTO;
import com.bewg.pd.baseinfo.modules.entity.vo.SupportValidateVO;
import com.bewg.pd.baseinfo.modules.entity.vo.TemplateWorkbookVO;
import com.bewg.pd.baseinfo.modules.entity.vo.WorkBookDetailsVO;
import com.bewg.pd.baseinfo.modules.entity.vo.WorkBookParseVO;
import com.bewg.pd.common.entity.excel.Sheet;
import com.bewg.pd.common.entity.req.WorkbookCalcReq;
import com.bewg.pd.common.entity.vo.Result;

/**
 * 计算书模板
 *
 * @author dongbd
 * @date 2021-11-01 17:37
 **/
public interface ITemplateWorkbookService extends IService<TemplateWorkbook> {

    Result<Object> isAvailable(Long id, Integer candidate);

    Result<List<TemplateWorkbookVO>> queryTemplateWorkbook(Long productMemberId);

    int deleteByProductMemberId(Long productMemberId);

    Result<Long> upLoadExcel(MultipartFile file, Long productMemberId, Long createBy);

    Result<SupportValidateVO> validateExcel(Long id);

    Result<?> validateAndCreatContext(Long id, String contextId);

    Result<WorkBookParseVO> parseExcel(Long id);

    Result<String> creatContext(Long id);

    Result<Long> releaseExcel(MultipartFile[] files, ReleaseTemplateDTO releaseTemplateDTO);

    Result<?> removeWorkFile(Long id);

    Result<WorkBookDetailsVO> getDetailsById(Long id);

    Result<Sheet> calculateExcel(WorkbookCalcReq workbookCalcReq);

    Result<?> cleanGarbageFiles(RemoveTemplateDTO removeTemplateDTO);

    TemplateWorkbook selectEnabledVersion(Long productMemberId);
}
