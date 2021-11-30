package com.bewg.pd.baseinfo.modules.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bewg.pd.baseinfo.modules.entity.AttachedFile;
import com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook;
import com.bewg.pd.baseinfo.modules.entity.dto.AttachedFileDTO;
import com.bewg.pd.baseinfo.modules.entity.vo.AttachedFileVO;
import com.bewg.pd.common.entity.vo.Result;

/**
 * 模板附属文件表
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
public interface IAttachedFileService extends IService<AttachedFile> {

    Result<?> upLoadImage(MultipartFile[] files, AttachedFileDTO attachedFileDTO, List<Long> imageIdList);

    Result<Long> upLoadAnnex(MultipartFile[] files, AttachedFileDTO attachedFileDTO);

    void removeAttachedFiles(AttachedFileDTO attachedFileDTO);

    void updateTemplateWorkbookId(TemplateWorkbook templateWorkbook, Long newTemplateWorkbookId);

    void releaseAnnexStatus(AttachedFileDTO attachedFileDTO, Long annexId);

    List<AttachedFileVO> getListByTemplateWorkbookId(Long templateWorkbookId, String fileType);

    void removeAnnex(Long id);
}
