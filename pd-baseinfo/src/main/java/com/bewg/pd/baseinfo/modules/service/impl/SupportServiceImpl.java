package com.bewg.pd.baseinfo.modules.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook;
import com.bewg.pd.baseinfo.modules.entity.vo.SupportVo;
import com.bewg.pd.baseinfo.modules.mapper.SupportMapper;
import com.bewg.pd.baseinfo.modules.mapper.TemplateSupportRelMapper;
import com.bewg.pd.baseinfo.modules.mapper.TemplateWorkbookMapper;
import com.bewg.pd.baseinfo.modules.service.IProductMemberService;
import com.bewg.pd.baseinfo.modules.service.ISupportService;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.util.CommonUtils;
import com.bewg.pd.common.util.ParamCheckUtil;

/**
 * @Description: 辅助数据表
 * @Author: Zhaoyubo
 * @Date: 2021-10-19
 * @Version: V1.0
 */
@Service
public class SupportServiceImpl extends ServiceImpl<SupportMapper, Support> implements ISupportService {
    @Autowired
    private SupportMapper supportMapper;

    @Autowired
    private TemplateSupportRelMapper templateSupportRelMapper;

    @Autowired
    private TemplateWorkbookMapper templateWorkbookMapper;

    private static SupportVo supportVo;

    @Autowired
    private IProductMemberService productMemberService;

    /**
     * 新增辅助数据表
     *
     * @return com.bewg.pd.common.vo.Result
     */
    @Override
    public Result supportAdd(MultipartFile file, Long productLineId) {
        Support support = new Support();
        String excelName = getFileName(file);
        // checkExcelName(excelName);
        String fileTyle = getFileTyle(file);
        Result<Object> result = new Result<>();
        if (fileTyle.toLowerCase().equals(".xlsx") || fileTyle.toLowerCase().equals(".xlsm")) {
            QueryWrapper<Support> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("excel_name", excelName);
            queryWrapper.eq("product_line_id", productLineId);
            Long count = supportMapper.selectCount(queryWrapper);
            boolean isUniqueName = count == 0;
            if (isUniqueName) {
                String supportUrl = upload(file);
                support.setExcelName(excelName);
                support.setFileExtension(fileTyle);
                support.setExcelUrl(supportUrl);
                support.setProductLineId(productLineId);
                supportMapper.insert(support);
                result.setResult(support.getId());
                result.success("新增成功");
            } else {
                throw new PdException("文件已存在，请修改");
            }
        } else {
            log.error("上传文件格式错误");
            throw new PdException("上传文件格式错误");
        }
        return result;
    }

    /**
     * 更新辅助数据表
     */
    @Override
    public Result supportUpdate(MultipartFile file, Long id, Long productLineId) {
        Result result = new Result();
        Support support = supportMapper.selectById(id);
        if (support == null) {
            throw new PdException("不存在该辅助数据表，请检查");
        }
        String supportExcelName = support.getExcelName();
        String excelName = getFileName(file);
        if (!supportExcelName.equals(excelName)) {
            throw new PdException("更新操作只接受同名文件，请检查");
        }
        // checkExcelName(excelName);
        String fileTyle = getFileTyle(file);
        if (fileTyle.toLowerCase().equals(".xlsx") || fileTyle.toLowerCase().equals(".xlsm")) {
            try {
                CommonUtils.deleteFile(support.getExcelUrl(), CommonConstant.UPLOAD_TYPE_FASTDFS);
            } catch (UnsupportedEncodingException e) {
                throw new PdException(e.getMessage());
            }
            String supportUrl = upload(file);
            // 服务器路径+存储路径
            support.setId(id);
            support.setExcelName(excelName);
            support.setExcelUrl(supportUrl);
            support.setProductLineId(productLineId);
            support.setUpdateTime(new Date());
            supportMapper.updateById(support);
        } else {
            log.error("上传文件格式错误");
            throw new PdException("上传文件格式错误");
        }
        List<Long> templateIdList = templateSupportRelMapper.findTemplateId(id);
        if (templateIdList != null) {
            for (Long templateId : templateIdList) {
                TemplateWorkbook templateWorkbook = templateWorkbookMapper.selectById(templateId);
                templateWorkbook.setIsVerified(0);
                templateWorkbook.setIsCandidate(0);
                templateWorkbookMapper.updateById(templateWorkbook);
            }
        }
        result.setResult(support.getId());
        result.success("更新成功");
        return result;
    }

    /**
     * 查看名称是否以"辅助_"开头
     * 
     * @author Zhaoyubo
     * @date 2021/11/01 09:34
     * @param excelName
     * @return boolean
     */
    void checkExcelName(String excelName) {
        String exFileName = excelName.substring(0, 3);
        String name = "辅助_";
        if (!name.equals(exFileName)) {
            throw new PdException("辅助数据表名称必须以“辅助_”开头，请修改");
        }
    }

    /**
     * 获得不带后缀的文件名
     * 
     * @author Zhaoyubo
     * @date 2021/11/10 17:31
     * @param file
     * @return java.lang.String
     */
    public String getFileName(MultipartFile file) {
        String fullName = file.getOriginalFilename();
        String fileName = fullName.substring(0, fullName.lastIndexOf("."));
        return fileName;
    }

    /**
     * 获得后缀名
     * 
     * @author Zhaoyubo
     * @date 2021/10/29 17:12
     * @param file
     * @return java.lang.String
     */
    public String getFileTyle(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileTyle = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        return fileTyle;
    }

    /**
     * 文件上传
     */
    public String upload(MultipartFile file) {
        return CommonUtils.upload(file, CommonConstant.UPLOAD_TYPE_FASTDFS);
    }

    /**
     * 下载辅助数据表
     */
    @Override
    public Result downloadSheet(String excelName, String excelUrl, String fileExtension, HttpServletResponse response) throws UnsupportedEncodingException {
        Result result = new Result<>();
        CommonUtils.download(excelName, excelUrl, fileExtension, CommonConstant.UPLOAD_TYPE_FASTDFS, response);
        result.success("下载成功");
        return result;
    }

    /**
     * 删除辅助数据表
     */
    @Override
    public Result deleteSheet(Long id) {
        try {
            Support support = supportMapper.selectById(id);
            Result result = new Result<>();
            if (support == null) {
                throw new PdException("删除失败，文件不存在");
            }
            int refNumber = supportMapper.findRefNumber(id);
            if (refNumber != 0) {
                throw new PdException("删除失败，该表被引用");
            }
            CommonUtils.deleteFile(support.getExcelUrl(), CommonConstant.UPLOAD_TYPE_FASTDFS);
            supportMapper.deleteById(id);
            return result.success("删除成功");
        } catch (Exception ex) {
            throw new PdException(ex.getMessage());
        }
    }

    /**
     * 展示辅助数据表树状结构
     */
    @Override
    public Result<List<SupportVo>> findSupport(Long productLineId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Result result = new Result();
        queryWrapper.eq("product_line_id", productLineId);
        queryWrapper.orderByDesc("update_time");
        List<Support> list = supportMapper.selectList(queryWrapper);
        if (list == null) {
            return result.success("该产品线下没有辅助数据表");
        }
        List<SupportVo> voList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Support support = list.get(i);
            Long id = support.getId();
            supportVo = new SupportVo();
            int refNumber = supportMapper.findRefNumber(id);
            supportVo.setRefNumber(refNumber);
            BeanUtils.copyProperties(support, supportVo);
            voList.add(supportVo);
        }
        result.setResult(voList);
        return result.success("成功展示辅助数据表树状结构");
    }

    /**
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
    @Override
    public Result<?> uploadSupport(MultipartFile file, String targetName, Long productMemberId) {
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!".xlsx".equalsIgnoreCase(fileExtension) && !".xlsm".equalsIgnoreCase(fileExtension)) {
            throw new PdException("上传文件格式错误");
        }
        String excelName = getFileName(file);
        if (!excelName.equals(targetName)) {
            throw new PdException("需要上传辅助数据表:" + targetName);
        }
        Long productLineId = productMemberService.selectProductLineId(productMemberId);
        if (ParamCheckUtil.isEmpty(productLineId)) {
            throw new PdException("未找到对应的产品线");
        }
        // checkExcelName(file.getOriginalFilename());
        QueryWrapper<Support> queryWrapper = new QueryWrapper<Support>().eq("excel_name", excelName).eq("product_line_id", productLineId).eq("is_del", 0);
        if (supportMapper.selectCount(queryWrapper) > 0) {
            // 如果有名称重复的数据就不再继续上传
            return Result.ok(true);
        }

        String uploadUrl = CommonUtils.upload(file, CommonConstant.UPLOAD_TYPE_FASTDFS);
        Support support = new Support();
        support.setExcelName(excelName);
        support.setFileExtension(fileExtension);
        support.setExcelUrl(uploadUrl);
        support.setProductLineId(productLineId);
        supportMapper.insert(support);
        return Result.ok(true);
    }

    /**
     * 获得产品线id下的所有辅助数据表名称
     * 
     * @author Zhaoyubo
     * @date 2021/10/29 14:50
     * @param productLineId
     * @return java.util.List<java.lang.String>
     */
    public List<String> getExcelNames(Long productLineId) {
        List<String> excelNames = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_line_id", productLineId);
        List<Support> list = supportMapper.selectList(queryWrapper);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Support support = list.get(i);
                String excelName = support.getExcelName();
                excelNames.add(excelName);
            }
        }
        return excelNames;
    }
}
