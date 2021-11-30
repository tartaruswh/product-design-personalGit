package com.bewg.pd.baseinfo.modules.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewg.pd.baseinfo.modules.entity.ProductMember;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.TemplateSupportRel;
import com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook;
import com.bewg.pd.baseinfo.modules.entity.dto.AttachedFileDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.ReleaseTemplateDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.RemoveTemplateDTO;
import com.bewg.pd.baseinfo.modules.entity.vo.*;
import com.bewg.pd.baseinfo.modules.mapper.SupportMapper;
import com.bewg.pd.baseinfo.modules.mapper.TemplateWorkbookMapper;
import com.bewg.pd.baseinfo.modules.service.IAttachedFileService;
import com.bewg.pd.baseinfo.modules.service.IProductMemberService;
import com.bewg.pd.baseinfo.modules.service.ITemplateSupportRelService;
import com.bewg.pd.baseinfo.modules.service.ITemplateWorkbookService;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.entity.excel.Sheet;
import com.bewg.pd.common.entity.req.WorkbookCalcReq;
import com.bewg.pd.common.entity.req.WorkbookContextReq;
import com.bewg.pd.common.entity.req.WorkbookFileReq;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.util.CommonUtils;
import com.bewg.pd.common.util.OrikaUtils;
import com.bewg.pd.common.util.ParamCheckUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 计算书模板
 *
 * @author dongbd
 * @date 2021-11-01 17:37
 **/
@Service
@Slf4j
public class TemplateWorkbookServiceImpl extends ServiceImpl<TemplateWorkbookMapper, TemplateWorkbook> implements ITemplateWorkbookService {
    private static final String workBookUrl = "http://10.10.41.3:8092/v1/workbook";

    /*static {
        try {
            workBookUrl = "http://" + InetAddress.getLocalHost().getHostAddress() + ":8092/v1/workbook";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }*/

    @Autowired
    private TemplateWorkbookMapper templateWorkbookMapper;

    @Autowired
    private SupportMapper supportMapper;

    @Autowired
    private IAttachedFileService attachedFileService;

    @Autowired
    private ITemplateSupportRelService templateSupportRelService;

    @Autowired
    private IProductMemberService productMemberService;

    /**
     * 设置计算书的可用状态
     *
     * @param id
     *            主键ID
     * @param candidate
     *            计算书可用状态
     * @return void
     * @author Zhaoyubo
     * @date 2021/11/03 09:44
     */
    @Override
    public Result<Object> isAvailable(Long id, Integer candidate) {
        TemplateWorkbook templateWorkbook = templateWorkbookMapper.selectById(id);
        if (templateWorkbook != null) {
            if (candidate == 0) {
                templateWorkbook.setIsCandidate(0);
                templateWorkbookMapper.updateById(templateWorkbook);
            } else if (candidate == 1) {
                Integer isVerified = templateWorkbook.getIsVerified();
                if (isVerified == 0) {
                    throw new PdException("请先进行验证，通过之后再切换使用状态");
                }
                Long productMemberId = templateWorkbook.getProductMemberId();
                QueryWrapper<TemplateWorkbook> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("product_member_id", productMemberId);
                queryWrapper.eq("is_candidate", candidate);
                List<TemplateWorkbook> templateWorkbookList = templateWorkbookMapper.selectList(queryWrapper);
                if (templateWorkbookList.size() > 1) {
                    throw new PdException("状态错误，只允许一个版本可用");
                } else if (templateWorkbookList.size() == 1) {
                    TemplateWorkbook templateWorkbook1 = templateWorkbookList.get(0).setIsCandidate(0);
                    templateWorkbook.setIsCandidate(candidate);
                    templateWorkbookMapper.updateById(templateWorkbook1);
                    templateWorkbookMapper.updateById(templateWorkbook);
                } else {
                    templateWorkbook.setIsCandidate(candidate);
                    templateWorkbookMapper.updateById(templateWorkbook);
                }
            } else {
                throw new PdException("参数错误，请检查");
            }
        } else {
            throw new PdException("该版本不存在");
        }
        return Result.ok("设置状态成功");
    }

    /**
     * 查看计算书版本列表
     * 
     * @param productMemberId
     *            单体类型主键Id
     * @return com.bewg.pd.common.entity.vo.Result
     * @author Zhaoyubo
     * @date 2021/11/04 10:17
     */
    @Override
    public Result<List<TemplateWorkbookVO>> queryTemplateWorkbook(Long productMemberId) {
        Result<List<TemplateWorkbookVO>> result = new Result<>();
        TemplateWorkbookVO templateWorkbookVO;
        List<TemplateWorkbookVO> templateWorkbookVOList = new ArrayList<>();
        QueryWrapper<TemplateWorkbook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_member_id", productMemberId);
        List<TemplateWorkbook> templateWorkbookList = templateWorkbookMapper.selectList(queryWrapper);
        if (templateWorkbookList == null) {
            return result.success("该单体类型下不存在计算书版本");
        }
        for (TemplateWorkbook templateWorkbook : templateWorkbookList) {
            String version = templateWorkbook.getVersion();
            // TODO 产品方案未开始，先给个数据
            // int refNumber = templateWorkbookMapper.findRefNumber(version);
            templateWorkbookVO = new TemplateWorkbookVO();
            // templateWorkbookVO.setRefNumber(refNumber);
            BeanUtils.copyProperties(templateWorkbook, templateWorkbookVO);
            templateWorkbookVOList.add(templateWorkbookVO);
        }
        result.setResult(templateWorkbookVOList);
        return result.success("成功查看计算书版本列表");
    }

    /**
     * 删除单体类型下未发布的模板和文件
     *
     * @author dongbd
     * @date 2021/11/11 11:17
     * @param productMemberId
     *            单体类型id
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByProductMemberId(Long productMemberId) {
        // 查询该单体类型下的所有历史版本
        QueryWrapper<TemplateWorkbook> queryWrapper = new QueryWrapper<TemplateWorkbook>().eq("product_member_id", productMemberId).eq("is_del", 0);
        List<TemplateWorkbook> templateWorkbooks = templateWorkbookMapper.selectList(queryWrapper);
        // 没有的话直接返回
        if (CollectionUtils.isEmpty(templateWorkbooks)) {
            return 0;
        }
        // 判断是否有已发布的版本, 有的话不能删
        List<TemplateWorkbook> noReleaseList = templateWorkbooks.stream().filter(templateWorkbook -> templateWorkbook.getReleaseStatus().equals(0)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(noReleaseList) || noReleaseList.size() < templateWorkbooks.size()) {
            return 1;
        }
        // 删除未发布版本所关联的垃圾文件
        for (TemplateWorkbook templateWorkbook : noReleaseList) {
            removeExcel(templateWorkbook);
            templateWorkbookMapper.deleteById(templateWorkbook);
        }
        AttachedFileDTO attachedFileDTO = new AttachedFileDTO();
        attachedFileDTO.setProductMemberId(productMemberId);
        attachedFileService.removeAttachedFiles(attachedFileDTO);
        return 0;
    }

    /**
     * 清理未发布的垃圾文件
     *
     * @author dongbd
     * @date 2021/11/19 17:38
     * @param removeTemplateDTO
     *            删除计算书版本所需参数
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> cleanGarbageFiles(RemoveTemplateDTO removeTemplateDTO) {
        if ("EDIT".equals(removeTemplateDTO.getBtnType()) && ParamCheckUtil.isEmpty(removeTemplateDTO.getId())) {
            throw new PdException("计算书模板主键id不能为空");
        }

        if ("ADD".equals(removeTemplateDTO.getBtnType())) {
            TemplateWorkbook templateWorkbook = new TemplateWorkbook();
            if (ParamCheckUtil.isNotEmpty(removeTemplateDTO.getId())) {
                // 新增时点返回按钮 删模板
                templateWorkbook = selectById(removeTemplateDTO.getId());
            } else {
                QueryWrapper<TemplateWorkbook> queryWrapper =
                    new QueryWrapper<TemplateWorkbook>().eq("product_member_id", removeTemplateDTO.getProductMemberId()).eq("create_by", removeTemplateDTO.getCreateBy()).eq("release_status", 0).eq("is_del", 0);
                templateWorkbook = templateWorkbookMapper.selectOne(queryWrapper);
            }
            if (templateWorkbook != null) {
                if (StringUtils.isNotBlank(templateWorkbook.getFilePath())) {
                    removeExcel(templateWorkbook);
                }
                removeTemplateDTO.setId(templateWorkbook.getId());
                templateWorkbookMapper.deleteById(templateWorkbook.getId());
            } else {
                removeTemplateDTO.setId(0L);
            }
        }
        // 删除未发布的附件
        AttachedFileDTO attachedFileDTO = new AttachedFileDTO();
        attachedFileDTO.setTemplateWorkbookId(removeTemplateDTO.getId());
        attachedFileDTO.setType("ANNEX");
        attachedFileDTO.setReleaseStatus(0);
        attachedFileDTO.setCreateBy(removeTemplateDTO.getCreateBy());
        attachedFileDTO.setProductMemberId(removeTemplateDTO.getProductMemberId());
        attachedFileService.removeAttachedFiles(attachedFileDTO);
        return Result.ok("删除成功");
    }

    /**
     * 查看单体类型下是否有可用的计算书版本
     *
     * @author dongbd
     * @date 2021/11/19 16:24
     * @param productMemberId
     *            单体类型id
     * @return com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook
     */
    @Override
    public TemplateWorkbook selectEnabledVersion(Long productMemberId) {
        QueryWrapper<TemplateWorkbook> queryWrapper = new QueryWrapper<TemplateWorkbook>().eq("product_member_id", productMemberId).eq("is_candidate", 1).eq("is_verified", 1).eq("release_status", 1).eq("is_del", 0);
        return templateWorkbookMapper.selectOne(queryWrapper);
    }

    /**
     * 上传计算书模板、保存相关数据
     *
     * @param file
     *            计算书模板excel
     * @param productMemberId
     *            单体类型Id
     * @param createBy
     *            用户Id
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> upLoadExcel(MultipartFile file, Long productMemberId, Long createBy) {
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (!".xlsx".equalsIgnoreCase(fileExtension) && !".xlsm".equalsIgnoreCase(fileExtension)) {
            throw new PdException("上传文件格式错误");
        }

        // 如果该用户在该单体类型有已上传的模板Excel, 先删除文件
        QueryWrapper<TemplateWorkbook> queryWrapper = new QueryWrapper<TemplateWorkbook>().eq("release_status", 0).eq("product_member_id", productMemberId).eq("create_by", createBy).eq("is_del", 0);
        TemplateWorkbook templateWorkbook = templateWorkbookMapper.selectOne(queryWrapper);
        if (templateWorkbook != null) {
            removeExcel(templateWorkbook);
        } else {
            templateWorkbook = new TemplateWorkbook();
            templateWorkbook.setCreateBy(createBy);
            templateWorkbook.setIsCandidate(0);
            templateWorkbook.setIsVerified(0);
            templateWorkbook.setReleaseStatus(0);
            templateWorkbook.setProductMemberId(productMemberId);
        }

        // 上传计算书模板
        log.info("开始上传计算书模板: {}", file.getOriginalFilename());
        String uploadPath = CommonUtils.upload(file, CommonConstant.UPLOAD_TYPE_FASTDFS);
        System.out.println(uploadPath);
        if (StringUtils.isBlank(uploadPath)) {
            throw new PdException("上传文件失败");
        }
        templateWorkbook.setFileName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
        templateWorkbook.setFileExtension(fileExtension);
        templateWorkbook.setFilePath(uploadPath);

        if (ParamCheckUtil.isNotEmpty(templateWorkbook.getId())) {
            log.info("更新计算书模板{}", templateWorkbook);
            templateWorkbookMapper.updateById(templateWorkbook);
        } else {
            log.info("保存计算书模板{}", templateWorkbook);
            templateWorkbookMapper.insert(templateWorkbook);
            // 根据单体类型id和用户id更新有模板id为空的附属文件
            attachedFileService.updateTemplateWorkbookId(templateWorkbook, templateWorkbook.getId());
        }
        Result<Long> result = new Result<>();
        result.setResult(templateWorkbook.getId());
        result.setMessage("上传成功");
        result.setCode(200);
        result.setSuccess(true);
        return result;
    }

    /**
     * 验证计算书模板
     *
     * @author dongbd
     * @date 2021/11/08 10:00
     * @param id
     *            计算书模板Id
     * @return Result<SupportValidateVO>
     */
    @Override
    public Result<SupportValidateVO> validateExcel(Long id) {
        TemplateWorkbook templateWorkbook = selectById(id);
        if (StringUtils.isBlank(templateWorkbook.getFilePath())) {
            throw new PdException("未找到对应计算书模板");
        }

        Result<List<String>> workBookResult = getSupportNames(templateWorkbook.getFilePath());

        if (workBookResult != null && workBookResult.getCode().equals(200)) {
            SupportValidateVO supportValidateVO = new SupportValidateVO();
            supportValidateVO.setIsVerified(1);

            // 根据外部数据源名称和单体类型id验证是否存在
            if (CollectionUtils.isNotEmpty(workBookResult.getResult())) {
                List<String> supportList = workBookResult.getResult();
                Map<String, Integer> resultMap = new HashMap<>();
                for (String supportName : supportList) {
                    String name = supportName.substring(0, supportName.lastIndexOf("."));
                    Long productLineId = productMemberService.selectProductLineId(templateWorkbook.getProductMemberId());
                    QueryWrapper<Support> queryWrapper = new QueryWrapper<Support>().eq("product_line_id", productLineId).eq("excel_name", name).eq("is_del", 0);
                    Long count = supportMapper.selectCount(queryWrapper);
                    if (count == 1) {
                        resultMap.put(name, 1);
                    } else if (count == 0) {
                        resultMap.put(name, 0);
                        supportValidateVO.setIsVerified(0);
                    } else {
                        throw new PdException("辅助数据表重复数据无法识别");
                    }
                    supportValidateVO.setResultMap(resultMap);
                }
            }
            Result<SupportValidateVO> result = new Result<>();
            result.setResult(supportValidateVO);
            result.setCode(200);
            result.setMessage("验证请求正常结束");
            result.setSuccess(true);
            return result;
        } else {
            throw new PdException("验证请求失败");
        }
    }

    /**
     * 验证计算书模板并生成上下文
     *
     * @author dongbd
     * @date 2021/11/05 13:35
     * @param id
     *            计算书模板id
     * @return com.bewg.pd.common.entity.vo.Result<String>
     */
    @Override
    public Result<?> validateAndCreatContext(Long id, String contextId) {
        Result<SupportValidateVO> validateResult = validateExcel(id);
        if (!validateResult.getCode().equals(200) || validateResult.getResult() == null || validateResult.getResult().getIsVerified().equals(0)) {
            throw new PdException("计算书模板验证未通过");
        }
        if (StringUtils.isBlank(contextId)) {
            return creatContext(id);
        }
        Result<String> result = new Result<>();
        result.setResult(contextId);
        result.setMessage("上下文创建成功");
        result.setCode(200);
        result.setSuccess(true);
        return result;
    }

    /**
     * 解析计算书模板Excel
     *
     * @author dongbd
     * @date 2021/11/05 13:35
     * @param id
     *            计算书模板id
     * @return com.bewg.pd.common.entity.vo.Result<WorkBookParseVO>
     */
    @Override
    public Result<WorkBookParseVO> parseExcel(Long id) {
        TemplateWorkbook templateWorkbook = selectById(id);
        if (StringUtils.isBlank(templateWorkbook.getFilePath())) {
            throw new PdException("未找到对应计算书模板");
        }
        WorkBookParseVO workBookParseVO = new WorkBookParseVO();
        if (templateWorkbook.getReleaseStatus().equals(1)) {
            List<AttachedFileVO> attachedFileVOList = attachedFileService.getListByTemplateWorkbookId(id, "IMAGE");
            workBookParseVO.setImageList(attachedFileVOList);
        }
        // 拼接请求workBook解析所需要的参数
        WorkbookFileReq workbookFileReq = new WorkbookFileReq();
        workbookFileReq.setFilePath(templateWorkbook.getFilePath());
        ParameterizedTypeReference<Result<Sheet>> typeRef = new ParameterizedTypeReference<Result<Sheet>>() {};
        Result<Sheet> parseResult = (Result<Sheet>)exchange(workBookUrl + "/workbook/parse", workbookFileReq, typeRef).getBody();
        if (parseResult != null && parseResult.getCode().equals(200) && parseResult.getResult() != null) {
            workBookParseVO.setSheet(parseResult.getResult());
        }
        Result<WorkBookParseVO> result = new Result<>();
        result.setCode(200);
        result.setSuccess(true);
        result.setMessage("解析成功");
        result.setResult(workBookParseVO);
        return result;
    }

    /**
     * 创建上下文
     *
     * @author dongbd
     * @date 2021/11/10 17:07
     * @param id
     *            计算书模板Id
     * @return Result<String>
     */
    @Override
    public Result<String> creatContext(Long id) {
        TemplateWorkbook templateWorkbook = selectById(id);
        if (StringUtils.isBlank(templateWorkbook.getFilePath())) {
            throw new PdException("未找到对应计算书模板");
        }
        WorkbookContextReq workbookContextReq = new WorkbookContextReq();
        workbookContextReq.setDocName(templateWorkbook.getFileName() + templateWorkbook.getFileExtension());
        workbookContextReq.setDocPath(templateWorkbook.getFilePath());
        Long productLineId = productMemberService.selectProductLineId(templateWorkbook.getProductMemberId());
        if (ParamCheckUtil.isEmpty(productLineId)) {
            throw new PdException("未找到对应产品线");
        }
        workbookContextReq.setProductMemberId(productLineId.toString());

        List<WorkbookContextReq.ParamPair> paramPairs = new ArrayList<>();
        if (templateWorkbook.getReleaseStatus().equals(0)) {
            // 新增页面创建上下文， 需要调用workbook解析出所引用的辅助数据表名字
            log.info("新增页面创建上下文");
            Result<List<String>> supportNames = getSupportNames(templateWorkbook.getFilePath());
            if (supportNames != null && supportNames.getCode() == 200) {
                if (CollectionUtils.isNotEmpty(supportNames.getResult())) {
                    for (String supportName : supportNames.getResult()) {
                        QueryWrapper<Support> queryWrapper = new QueryWrapper<Support>().eq("product_line_id", productLineId).eq("excel_name", supportName.substring(0, supportName.lastIndexOf("."))).eq("is_del", 0);
                        Support support = supportMapper.selectOne(queryWrapper);
                        if (support == null) {
                            throw new PdException("无法创建上下文: 未找到[" + supportName + "]");
                        }
                        WorkbookContextReq.ParamPair paramPair = new WorkbookContextReq.ParamPair();
                        paramPair.setDocName(supportName);
                        paramPair.setDocPath(support.getExcelUrl());
                        paramPairs.add(paramPair);
                    }
                }
                workbookContextReq.setAssociatedDocPath(paramPairs);
            }
        } else {
            // 编辑页面创建上下文， 需要通过模板辅助数据表的中间表查出所引用的辅助数据表名字
            log.info("编辑页面创建上下文");
            if (templateWorkbook.getReleaseStatus().equals(1)) {
                List<Support> supports = templateSupportRelService.getSupportsByTempalteId(id);
                if (CollectionUtils.isNotEmpty(supports)) {
                    for (Support support : supports) {
                        WorkbookContextReq.ParamPair paramPair = new WorkbookContextReq.ParamPair();
                        paramPair.setDocName(support.getExcelName() + support.getFileExtension());
                        paramPair.setDocPath(support.getExcelUrl());
                        paramPairs.add(paramPair);
                    }
                    workbookContextReq.setAssociatedDocPath(paramPairs);
                }
            }
        }
        // 调用workbook创建上下文
        ParameterizedTypeReference<Result<String>> typeRef = new ParameterizedTypeReference<Result<String>>() {};
        return (Result<String>)exchange(workBookUrl + "/workbook/context/create", workbookContextReq, typeRef).getBody();
    }

    /**
     * 驱动计算计算书模板
     *
     * @param workbookCalcReq
     *            workbookCalcReq
     * @return Result<Sheet>
     */
    @Override
    public Result<Sheet> calculateExcel(WorkbookCalcReq workbookCalcReq) {
        ParameterizedTypeReference<Result<Sheet>> typeRef = new ParameterizedTypeReference<Result<Sheet>>() {};
        return (Result<Sheet>)exchange(workBookUrl + "/workbook/calculate", workbookCalcReq, typeRef).getBody();
    }

    /**
     * 发布版本， 生成版本号
     *
     * @author dongbd
     * @date 2021/11/05 13:59
     * @param files
     *            导图集合
     * @param releaseTemplateDTO
     *            模板发布所需参数
     * @return com.bewg.pd.common.entity.vo.Result<?>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> releaseExcel(MultipartFile[] files, ReleaseTemplateDTO releaseTemplateDTO) {
        TemplateWorkbook templateWorkbook = selectById(releaseTemplateDTO.getId());

        // 发布前需要再次验证模板
        Result<SupportValidateVO> validateResult = validateExcel(releaseTemplateDTO.getId());
        if (validateResult.getCode() != 200 || validateResult.getResult().getIsVerified() == 0) {
            throw new PdException("计算书模板验证未通过");
        }
        // 删除计算书与辅助数据关系表数据
        templateSupportRelService.deleteByTemplateId(releaseTemplateDTO.getId());
        if (validateResult.getResult().getResultMap() != null) {
            Set<String> supportNames = validateResult.getResult().getResultMap().keySet();
            Iterator<String> iterator = supportNames.iterator();
            Long productLineId = productMemberService.selectProductLineId(templateWorkbook.getProductMemberId());
            if (iterator.hasNext()) {
                QueryWrapper<Support> queryWrapper = new QueryWrapper<Support>().eq("product_line_id", productLineId).eq("excel_name", iterator.next()).eq("is_del", 0);
                Support support = supportMapper.selectOne(queryWrapper);
                TemplateSupportRel templateSupportRel = new TemplateSupportRel();
                templateSupportRel.setSupportId(support.getId());
                templateSupportRel.setTemplateId(releaseTemplateDTO.getId());
                templateSupportRel.setCreateBy(releaseTemplateDTO.getCreateBy());
                templateSupportRelService.save(templateSupportRel);
            }
        }
        AttachedFileDTO attachedFileDTO = new AttachedFileDTO();
        attachedFileDTO.setTemplateWorkbookId(releaseTemplateDTO.getId());
        attachedFileDTO.setProductMemberId(templateWorkbook.getProductMemberId());
        attachedFileDTO.setReleaseStatus(templateWorkbook.getReleaseStatus());
        attachedFileDTO.setType("IMAGE");
        // 上传导图
        if (files != null) {
            for (MultipartFile file : files) {
                ParamCheckUtil.checkEmptyFile(file);
            }
            attachedFileService.upLoadImage(files, attachedFileDTO, releaseTemplateDTO.getImageIdList());
        }

        // 附件状态改为已发布
        attachedFileDTO.setType("ANNEX");
        attachedFileService.releaseAnnexStatus(attachedFileDTO, releaseTemplateDTO.getAnnexId());
        // 状态为未发布即为发布新版本
        if (templateWorkbook.getReleaseStatus().equals(0)) {
            // 修改发布状态及创建时间、生成计算书版本号
            templateWorkbook.setReleaseStatus(1);
            StringBuilder currentData = new StringBuilder(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            QueryWrapper<TemplateWorkbook> templateWorkbookQueryWrapper =
                new QueryWrapper<TemplateWorkbook>().eq("product_member_id", templateWorkbook.getProductMemberId()).eq("is_del", 0).eq("release_status", 1).orderByDesc("create_time");
            List<TemplateWorkbook> templateWorkbooks = templateWorkbookMapper.selectList(templateWorkbookQueryWrapper);
            if (templateWorkbooks.size() > 1 && templateWorkbooks.get(0).getVersion().startsWith(currentData.toString())) {
                templateWorkbook.setVersion(String.valueOf(Long.parseLong(templateWorkbooks.get(0).getVersion()) + 1));
            } else {
                templateWorkbook.setVersion(currentData.append("0001").toString());
            }
            templateWorkbook.setCreateTime(new Date());
        }
        templateWorkbook.setIsCandidate(0);
        templateWorkbook.setIsVerified(1);
        templateWorkbook.setVersionDescription(releaseTemplateDTO.getVersionDescription());
        int update = templateWorkbookMapper.updateById(templateWorkbook);
        Result<Long> result = new Result<>();
        if (update > 0) {
            result.setResult(templateWorkbook.getId());
            result.setMessage("发布成功");
            result.setCode(200);
            result.setSuccess(true);
        } else {
            throw new PdException("发布失败");
        }
        return result;
    }

    /**
     * 删除计算书模板及附属文件
     *
     * @author dongbd
     * @date 2021/11/04 16:34
     * @param id
     *            计算书模板主键id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> removeWorkFile(Long id) {
        // todo 被方案引用的版本不能删除

        TemplateWorkbook templateWorkbook = selectById(id);
        if (templateWorkbook.getReleaseStatus().equals(0)) {
            // 根据单体类型id和用户id更新附属文件模板id
            attachedFileService.updateTemplateWorkbookId(templateWorkbook, 0L);
        } else {
            // 删除计算书与辅助数据关系表数据
            templateSupportRelService.deleteByTemplateId(id);
            // 根据模板id删除所有附属文件
            AttachedFileDTO attachedFileDTO = new AttachedFileDTO();
            attachedFileDTO.setTemplateWorkbookId(id);
            attachedFileService.removeAttachedFiles(attachedFileDTO);

        }
        log.info("开始删除计算书模板Excel====>>>>名称:{}, URL:{}", templateWorkbook.getFileName(), templateWorkbook.getFilePath());
        removeExcel(templateWorkbook);
        templateWorkbookMapper.deleteById(id);
        return Result.ok("删除成功");
    }

    /**
     * 通过id查询历史版本详情
     *
     * @param id
     *            计算书模板Id
     * @return Result<WorkBookDetailsVO>
     */
    @Override
    public Result<WorkBookDetailsVO> getDetailsById(Long id) {
        // 计算书模板基本信息
        TemplateWorkbook templateWorkbook = selectById(id);
        WorkBookDetailsVO workBookDetailsVO = OrikaUtils.convert(templateWorkbook, WorkBookDetailsVO.class);
        // 单体类型基本信息
        ProductMember productMember = productMemberService.getById(templateWorkbook.getProductMemberId());
        if (productMember == null) {
            throw new PdException("未找到对应的单体类型");
        }
        workBookDetailsVO.setProductMemberId(productMember.getId());
        workBookDetailsVO.setMemberName(productMember.getMemberName());
        workBookDetailsVO.setBusinessCode(productMember.getBusinessCode());
        // 附属文件集合
        List<AttachedFileVO> attachedFileVOList = attachedFileService.getListByTemplateWorkbookId(id, null);
        if (CollectionUtils.isNotEmpty(attachedFileVOList)) {
            Map<String, List<AttachedFileVO>> group = attachedFileVOList.stream().collect(Collectors.groupingBy(AttachedFileVO::getType));
            workBookDetailsVO.setImageList(group.get("IMAGE") == null ? new ArrayList<>(0) : group.get("IMAGE"));
            workBookDetailsVO.setAnnexList(group.get("ANNEX") == null ? new ArrayList<>(0) : group.get("ANNEX"));
        }
        Result<WorkBookDetailsVO> result = new Result<>();
        result.setResult(workBookDetailsVO);
        result.setCode(200);
        result.setMessage("查询详情成功");
        return result;
    }

    private void removeExcel(TemplateWorkbook templateWorkbook) {
        if (StringUtils.isBlank(templateWorkbook.getFilePath())) {
            return;
        }
        try {
            log.info("开始删除计算书模板Excel : {}", templateWorkbook.getFileName() + templateWorkbook.getFileExtension());
            CommonUtils.deleteFile(templateWorkbook.getFilePath(), CommonConstant.UPLOAD_TYPE_FASTDFS);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            throw new PdException("删除计算书模板Excel失败");
        }
    }

    private TemplateWorkbook selectById(Long id) {
        TemplateWorkbook templateWorkbook = templateWorkbookMapper.selectById(id);
        if (templateWorkbook == null) {
            throw new PdException("未找到对应计算书模板");
        }
        return templateWorkbook;
    }

    ResponseEntity<?> exchange(String url, Object requestParams, ParameterizedTypeReference<?> typeRef) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);// 设置连接超时，单位毫秒
        requestFactory.setReadTimeout(60000); // 设置读取超时
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(requestParams), headers);
        log.info("发送请求:{}", url);
        log.info("请求参数:{}", httpEntity.getBody());
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, typeRef);
        log.info("响应结果:{}", exchange);
        return exchange;
    }

    private Result<List<String>> getSupportNames(String filePath) {
        // 调用workbook接口获取外部数据源
        WorkbookFileReq workbookFileReq = new WorkbookFileReq();
        workbookFileReq.setFilePath(filePath);
        ParameterizedTypeReference<Result<List<String>>> typeRef = new ParameterizedTypeReference<Result<List<String>>>() {};
        return (Result<List<String>>)exchange(workBookUrl + "/workbook/refs/parse", workbookFileReq, typeRef).getBody();

    }
}
