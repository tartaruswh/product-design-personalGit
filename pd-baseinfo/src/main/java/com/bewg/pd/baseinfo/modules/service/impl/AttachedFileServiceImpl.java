package com.bewg.pd.baseinfo.modules.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewg.pd.baseinfo.modules.entity.AttachedFile;
import com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook;
import com.bewg.pd.baseinfo.modules.entity.dto.AttachedFileDTO;
import com.bewg.pd.baseinfo.modules.entity.vo.AttachedFileVO;
import com.bewg.pd.baseinfo.modules.mapper.AttachedFileMapper;
import com.bewg.pd.baseinfo.modules.service.IAttachedFileService;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.util.CommonUtils;
import com.bewg.pd.common.util.OrikaUtils;
import com.bewg.pd.common.util.ParamCheckUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 模板附属文件表
 *
 * @author dongbd
 * @date 2021-11-04 14:00
 **/
@Service
@Slf4j
public class AttachedFileServiceImpl extends ServiceImpl<AttachedFileMapper, AttachedFile> implements IAttachedFileService {

    @Autowired
    private AttachedFileMapper attachedFileMapper;

    /**
     *
     * @author dongbd
     * @date 2021/11/11 12:24
     * @param files
     *            导图集合
     * @param attachedFileDTO
     *            上传导图所需参数
     * @param imageIdList
     *            保留的导图Id集合
     * @return com.bewg.pd.common.entity.vo.Result<?>
     */
    @Override
    public Result<?> upLoadImage(MultipartFile[] files, AttachedFileDTO attachedFileDTO, List<Long> imageIdList) {
        // 校验图片数组名称是否重复
        Set<String> names = Stream.of(files).map(MultipartFile::getOriginalFilename).collect(Collectors.toSet());
        if (names.size() < files.length) {
            throw new PdException("您不能上传名称重复的导图");
        }
        String imageExtension = "BMP/JPG/JPEG/PNG";
        for (String name : names) {
            if (!imageExtension.contains(name.substring(name.lastIndexOf(".") + 1).toUpperCase())) {
                throw new PdException("图片格式未识别");
            }
        }
        // 先删除已发布的旧导图
        if (attachedFileDTO.getReleaseStatus() == 1) {
            if (CollectionUtils.isNotEmpty(imageIdList)) {
                // 筛选出这次操作需要删除的旧的已发布的导图
                List<AttachedFile> attachedFiles = attachedFileMapper.selectAttachedFileList(attachedFileDTO);
                List<AttachedFile> attachedFileList = new ArrayList<>();
                for (AttachedFile attachedFile : attachedFiles) {
                    if (imageIdList.contains(attachedFile.getId())) {
                        int size = names.size();
                        names.add(attachedFile.getFileName() + attachedFile.getFileExtension());
                        if (size == names.size()) {
                            throw new PdException("您不能上传名称重复的导图");
                        }
                    } else {
                        attachedFileList.add(attachedFile);
                    }
                }
                deleteAttachedFiles(attachedFileList);
            } else {
                removeAttachedFiles(attachedFileDTO);
            }
        }
        // 上传新文件 保存数据
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            log.info("开始上传{}类型文件{}", attachedFileDTO.getType(), fileName);
            String uploadPath = CommonUtils.upload(file, CommonConstant.UPLOAD_TYPE_FASTDFS);
            if (StringUtils.isBlank(uploadPath)) {
                throw new PdException("上传文件失败");
            }
            AttachedFile attachedFile = OrikaUtils.convert(attachedFileDTO, AttachedFile.class);
            attachedFile.setReleaseStatus(1);
            attachedFile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
            attachedFile.setFileExtension(fileName.substring(file.getOriginalFilename().lastIndexOf(".")));
            attachedFile.setPath(uploadPath);
            attachedFile.setFileSize(getPrintSize(file.getSize()));
            attachedFileMapper.insert(attachedFile);
        }
        return null;
    }

    /**
     *
     * @author dongbd
     * @date 2021/11/11 12:24
     * @param files
     *            附件集合
     * @param attachedFileDTO
     *            上传附件所需参数
     * @return com.bewg.pd.common.entity.vo.Result<?>
     */
    @Override
    public Result<Long> upLoadAnnex(MultipartFile[] files, AttachedFileDTO attachedFileDTO) {
        // 校验附件个数
        if (files.length > 1) {
            throw new PdException("请勿上传多个附件");
        }
        // 先删除所有未发布的附件
        QueryWrapper<AttachedFile> attachedFileQueryWrapper = new QueryWrapper<AttachedFile>().eq("release_status", 0).eq("type", "ANNEX").eq("create_by", attachedFileDTO.getCreateBy()).eq("is_del", 0);
        if (ParamCheckUtil.isEmpty(attachedFileDTO.getTemplateWorkbookId())) {
            // templateWorkbookId为空即为新增时未上传模板的情况下 上传附件, 删除该用户该单体类型下的附件
            attachedFileQueryWrapper.eq("product_member_id", attachedFileDTO.getProductMemberId());
            attachedFileDTO.setTemplateWorkbookId(0L);
        } else {
            // templateWorkbookId不为空, 删除该用户该模板下的附件
            attachedFileQueryWrapper.eq("template_workbook_id", attachedFileDTO.getTemplateWorkbookId());
        }
        List<AttachedFile> attachedFiles = attachedFileMapper.selectList(attachedFileQueryWrapper);
        deleteAttachedFiles(attachedFiles);

        // 上传新文件 保存数据
        MultipartFile file = files[0];
        String fileName = file.getOriginalFilename();
        log.info("开始上传ANNEX类型文件{}", fileName);
        String uploadPath = CommonUtils.upload(file, CommonConstant.UPLOAD_TYPE_FASTDFS);
        if (StringUtils.isBlank(uploadPath)) {
            throw new PdException("上传附件失败");
        }
        AttachedFile attachedFile = OrikaUtils.convert(attachedFileDTO, AttachedFile.class);
        attachedFile.setType("ANNEX");
        attachedFile.setReleaseStatus(0);
        attachedFile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
        attachedFile.setFileExtension(fileName.substring(file.getOriginalFilename().lastIndexOf(".")));
        attachedFile.setPath(uploadPath);
        attachedFile.setFileSize(getPrintSize(file.getSize()));
        int insert = attachedFileMapper.insert(attachedFile);
        if (insert <= 0) {
            throw new PdException("上传附件失败");
        }
        Result<Long> result = new Result<>();
        result.setCode(200);
        result.setSuccess(true);
        result.setResult(attachedFile.getId());
        result.setMessage("上传附件成功");
        return result;
    }

    /**
     * 删除附属文件及数据
     * 
     * @author dongbd
     * @date 2021/11/11 10:52
     * @param attachedFileDTO
     *            attachedFileDTO
     */
    @Override
    public void removeAttachedFiles(AttachedFileDTO attachedFileDTO) {
        deleteAttachedFiles(attachedFileMapper.selectAttachedFileList(attachedFileDTO));
    }

    /**
     * 根据单体类型id和用户id更新附属文件模板id
     * 
     * @param templateWorkbook
     *            计算书模板
     */
    @Override
    public void updateTemplateWorkbookId(TemplateWorkbook templateWorkbook, Long newTemplateWorkbookId) {
        QueryWrapper<AttachedFile> attachedFileQueryWrapper = new QueryWrapper<AttachedFile>().eq("product_member_id", templateWorkbook.getProductMemberId()).eq("is_del", 0).eq("create_by", templateWorkbook.getCreateBy());
        if (ParamCheckUtil.isEmpty(newTemplateWorkbookId)) {
            attachedFileQueryWrapper.eq("template_workbook_id", templateWorkbook.getId());
        } else {
            attachedFileQueryWrapper.eq("template_workbook_id", 0);
        }
        List<AttachedFile> attachedFiles = attachedFileMapper.selectList(attachedFileQueryWrapper);
        if (CollectionUtils.isNotEmpty(attachedFiles)) {
            for (AttachedFile attachedFile : attachedFiles) {
                attachedFile.setTemplateWorkbookId(newTemplateWorkbookId);
                attachedFileMapper.updateById(attachedFile);
            }
        }
    }

    /**
     * 发布附件
     * 
     * @author dongbd
     * @date 2021/11/08 14:10
     * @param attachedFileDTO
     *            attachedFileDTO
     */
    @Override
    public void releaseAnnexStatus(AttachedFileDTO attachedFileDTO, Long annexId) {
        if (ParamCheckUtil.isEmpty(annexId)) {
            // 如果要保留的附件为空, 直接删除旧的已发布的附件
            attachedFileDTO.setReleaseStatus(1);
            removeAttachedFiles(attachedFileDTO);
        } else {
            AttachedFile attachedFile = attachedFileMapper.selectById(annexId);
            if (attachedFile.getReleaseStatus().equals(0)) {
                // 如果要保留的附件是未发布的, 先删除旧的已发布的附件
                attachedFileDTO.setReleaseStatus(1);
                removeAttachedFiles(attachedFileDTO);
                // 新的未发布的附件改为已发布
                attachedFile.setReleaseStatus(1);
                attachedFileMapper.updateById(attachedFile);
            }
        }
    }

    /**
     * 根据计算书模板id获取所有已发布附属文件
     * 
     * @param templateWorkbookId
     *            计算书模板Id
     * @param fileType
     *            文件类型
     * @return List<AttachedFileVO>
     */
    @Override
    public List<AttachedFileVO> getListByTemplateWorkbookId(Long templateWorkbookId, String fileType) {
        QueryWrapper<AttachedFile> queryWrapper = new QueryWrapper<AttachedFile>().eq("template_workbook_id", templateWorkbookId).eq("release_status", 1).eq("is_del", 0);
        if (fileType == null) {
            queryWrapper.eq("type", fileType);
        }
        List<AttachedFile> attachedFiles = attachedFileMapper.selectList(queryWrapper);
        return OrikaUtils.convertList(attachedFiles, AttachedFileVO.class);
    }

    /**
     * 通过id删除附件
     * 
     * @author dongbd
     * @date 2021/11/22 09:36
     * @param id
     *            主键id
     */
    @Override
    public void removeAnnex(Long id) {
        AttachedFile attachedFile = attachedFileMapper.selectById(id);
        List<AttachedFile> list = new ArrayList<>();
        list.add(attachedFile);
        deleteAttachedFiles(list);
    }

    /**
     * 循环删除文件及逻辑删除数据
     * 
     * @param attachedFiles
     *            文件记录集合
     */
    private void deleteAttachedFiles(List<AttachedFile> attachedFiles) {
        if (CollectionUtils.isNotEmpty(attachedFiles)) {
            for (AttachedFile attachedFile : attachedFiles) {
                log.info("开始删除{}类型文件{}", attachedFile.getType(), attachedFile.getFileName() + attachedFile.getFileExtension());
                if (StringUtils.isNotBlank(attachedFile.getPath())) {
                    try {
                        CommonUtils.deleteFile(attachedFile.getPath(), CommonConstant.UPLOAD_TYPE_FASTDFS);
                    } catch (UnsupportedEncodingException e) {
                        log.error(e.getMessage());
                        throw new PdException("文件删除失败");
                    }
                }
                attachedFileMapper.deleteById(attachedFile.getId());
            }
        }
    }

    /**
     * 计算文件大小
     * 
     * @param size
     *            size
     * @return String
     */
    private String getPrintSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return (size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return size / 100 + "." + size % 100 + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return size / 100 + "." + size % 100 + "GB";
        }
    }
}
