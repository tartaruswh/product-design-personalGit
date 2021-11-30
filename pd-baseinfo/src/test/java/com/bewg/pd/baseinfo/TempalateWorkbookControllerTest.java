package com.bewg.pd.baseinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bewg.pd.baseinfo.modules.entity.vo.WorkBookParseVO;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bewg.pd.baseinfo.modules.entity.ProductMember;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.dto.AttachedFileDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.ReleaseTemplateDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.RemoveTemplateDTO;
import com.bewg.pd.baseinfo.modules.entity.enums.MemberTypeEnum;
import com.bewg.pd.baseinfo.modules.entity.vo.SupportValidateVO;
import com.bewg.pd.baseinfo.modules.entity.vo.TemplateWorkbookVO;
import com.bewg.pd.baseinfo.modules.entity.vo.WorkBookDetailsVO;
import com.bewg.pd.baseinfo.modules.mapper.ProductMemberMapper;
import com.bewg.pd.baseinfo.modules.service.IAttachedFileService;
import com.bewg.pd.baseinfo.modules.service.ITemplateWorkbookService;
import com.bewg.pd.baseinfo.modules.service.impl.SupportServiceImpl;
import com.bewg.pd.common.entity.excel.Sheet;
import com.bewg.pd.common.entity.req.WorkbookCalcReq;
import com.bewg.pd.common.entity.vo.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 计算书模板测试类
 * 
 * @author dongbd
 * @date 2021/11/09 09:14
 **/
@Slf4j
@SpringBootTest(classes = BaseinfoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TempalateWorkbookControllerTest {
    private static final Long USERID = 2021110900000000001L;
    private static Long tempalateWorkbookID = 0L;
    private static Long productLineId = 0L;
    private static Long technologyStageId = 0L;
    private static Long monomerId = 0L;
    private static Long productMemberId = 0L;
    private static Long supportId = 0L;
    private static String contextId = null;
    @Autowired
    private ProductMemberMapper productMemberMapper;
    @Autowired
    private ITemplateWorkbookService templateWorkbookService;
    @Autowired
    private IAttachedFileService attachedFileService;
    @Autowired
    private SupportServiceImpl supportService;

    /**
     * 初始化单体类型
     * 
     * @author dongbd
     * @date 2021/11/09 09:17
     */
    @Test
    @Order(1)
    public void initProductMember() {
        log.info("初始化数据");
        // 初始化产品线,工艺阶段,单体类型
        ProductMember productLine = new ProductMember();
        productLine.setMemberName("产品线（测试历史版本）");
        productLine.setParentId(1000000000000000001L);
        productLine.setMemberOrder(100);
        productLine.setBusinessCode("测试");
        productLine.setType(MemberTypeEnum.PRODUCT_LINE.name());
        productMemberMapper.insert(productLine);
        productLineId = productLine.getId();
        log.info("初始化数据：产品线初始化成功：{}", productMemberMapper.selectById(productLineId));
        ProductMember technologyStage = new ProductMember();
        technologyStage.setMemberName("工艺阶段（测试历史版本）");
        technologyStage.setParentId(productLine.getId());
        technologyStage.setMemberOrder(1);
        technologyStage.setBusinessCode("测试");
        technologyStage.setType(MemberTypeEnum.TECHNOLOGY_STAGE.name());
        productMemberMapper.insert(technologyStage);
        technologyStageId = technologyStage.getId();
        log.info("初始化数据：工艺阶段初始化成功：{}", productMemberMapper.selectById(technologyStageId));
        ProductMember monomer = new ProductMember();
        monomer.setMemberName("单体（测试历史版本）");
        monomer.setParentId(technologyStage.getId());
        monomer.setMemberOrder(1);
        monomer.setBusinessCode("测试");
        monomer.setType(MemberTypeEnum.MONOMER.name());
        productMemberMapper.insert(monomer);
        monomerId = monomer.getId();
        log.info("初始化数据：单体初始化成功：{}", productMemberMapper.selectById(monomerId));
        ProductMember monomerType = new ProductMember();
        monomerType.setMemberName("单体类型（测试历史版本）");
        monomerType.setParentId(monomer.getId());
        monomerType.setMemberOrder(1);
        monomerType.setBusinessCode("测试");
        monomerType.setType(MemberTypeEnum.MONOMER_TYPE.name());
        productMemberMapper.insert(monomerType);
        productMemberId = monomerType.getId();
        log.info("初始化数据：单体类型初始化成功：{}", productMemberMapper.selectById(productMemberId));

        // 确保数据库中没有同名辅助数据表文件
        QueryWrapper<Support> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("excel_name", "进水提升泵房建模20210812");
        queryWrapper.eq("product_line_id", productLineId);
        Support oldSupport = supportService.getBaseMapper().selectOne(queryWrapper);
        if (oldSupport != null) {
            boolean result = supportService.removeById(oldSupport);
            log.info("删除辅助数据表：{}", result ? "成功" : "失败");
        }
    }

    /**
     * 未发布状态下通过单体类型Id和用户ID删除计算书模板及附属文件
     * 
     * @author dongbd
     * @date 2021/11/09 09:28
     */
    @Test
    @Order(2)
    public void deleteByProductMemberId() {
        log.info("删除未发布计算书模板及附件(跳转新增和编辑页面前先调用该接口)");
        RemoveTemplateDTO removeTemplateDTO = new RemoveTemplateDTO();
        removeTemplateDTO.setProductMemberId(productMemberId);
        removeTemplateDTO.setCreateBy(USERID);
        removeTemplateDTO.setBtnType("ADD");
        Result<?> result = templateWorkbookService.cleanGarbageFiles(removeTemplateDTO);
        log.info(result.getMessage());
    }

    /**
     * 上传计算书模板
     * 
     * @author dongbd
     * @date 2021/11/09 09:34
     */
    @Test
    @Order(3)
    public void upLoadExcel() {
        try {
            log.info("开始测试上传计算书模板");
            File file = new File("src/test/resources/计算书版本_计算书模板测试表.xlsm");
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), new FileInputStream(file));
            Result<Long> result = templateWorkbookService.upLoadExcel(multipartFile, productMemberId, USERID);
            if (result.getCode().equals(200)) {
                tempalateWorkbookID = result.getResult();
                log.info("上传计算书模板表成功，{}", templateWorkbookService.getDetailsById(tempalateWorkbookID).getResult());
            } else {
                log.error(result.getMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 验证计算书模板
     * 
     * @author dongbd
     * @date 2021/11/10 10:09
     */
    @Test
    @Order(4)
    public void validateExcel() {
        log.info("开始测试验证计算书模板");
        Result<SupportValidateVO> result = templateWorkbookService.validateExcel(tempalateWorkbookID);
        if (result.getCode().equals(200)) {
            log.info("验证计算书模板结果：{}", result.getResult().getIsVerified().equals(0) ? "通过" : "未通过");
            Iterator<Map.Entry<String, Integer>> iterator = result.getResult().getResultMap().entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<String, Integer> next = iterator.next();
                log.info("验证计算书模板结果展示：{}:{}", next.getKey(), next.getValue().equals(0) ? "通过" : "未通过");
                if (next.getValue().equals(0)) {
                    upLoadSupportExcel(next.getKey());
                }
            }
        } else {
            log.error(result.getMessage());
        }
    }

    /**
     * 上传辅助数据表(新增计算书版本页面接口)
     *
     * @author dongbd
     * @date 2021/11/18 15:40
     */
    private void upLoadSupportExcel(String targetName) {
        try {
            log.info("上传辅助数据表(新增计算书版本页面接口)");
            File file = new File("src/test/resources/进水提升泵房建模20210812.xlsm");
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            Result<?> result = supportService.uploadSupport(multipartFile, targetName, productMemberId);
            QueryWrapper<Support> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("excel_name", "进水提升泵房建模20210812");
            queryWrapper.eq("product_line_id", productLineId);
            Support support = supportService.getBaseMapper().selectOne(queryWrapper);
            supportId = support.getId();
            if (result.getCode().equals(200)) {
                log.info("上传辅助数据表(新增计算书版本页面接口)成功：{}", support);
            } else {
                log.error(result.getMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 创建上下文
     *
     * @author dongbd
     * @date 2021/11/10 10:09
     */
    @Test
    @Order(5)
    public void creatContext() {
        log.info("开始测试创建上下文");
        Result<String> result = templateWorkbookService.creatContext(tempalateWorkbookID);
        if (result.getCode().equals(200)) {
            contextId = result.getResult();
            log.info("创建上下文成功，contextId为{}", contextId);
        } else {
            log.error(result.getMessage());
        }

    }

    /**
     * 解析计算书模板
     * 
     * @author dongbd
     * @date 2021/11/10 10:09
     */
    @Test
    @Order(6)
    public void parseExcel() {
        log.info("开始测试解析计算书模板");
        Result<WorkBookParseVO> result = templateWorkbookService.parseExcel(tempalateWorkbookID);
        if (result.getCode().equals(200)) {
            log.info("解析计算书模板结果展示：{}", result.getResult());
        } else {
            log.error(result.getMessage());
        }
    }

    /**
     * 通过参数计算
     * 
     * @author dongbd
     * @date 2021/11/10 10:09
     */
    @Test
    @Order(7)
    public void calculateExcel() {
        log.info("开始测试驱动计算");
        WorkbookCalcReq.ParamPair paramPair = new WorkbookCalcReq.ParamPair();
        paramPair.setRequired(true);
        paramPair.setVal("80000");
        paramPair.setCoordinate("B4");
        WorkbookCalcReq workbookCalcReq = new WorkbookCalcReq();
        workbookCalcReq.setContextId(contextId);
        workbookCalcReq.setReqParam(paramPair);
        Result<Sheet> result = templateWorkbookService.calculateExcel(workbookCalcReq);
        if (result.getCode().equals(200)) {
            log.info("计算书模板计算结果展示：{}", result.getResult());
        } else {
            log.error(result.getMessage());
        }
    }

    /**
     * 上传附件
     * 
     * @author dongbd
     * @date 2021/11/10 09:18
     */
    @Test
    @Order(8)
    public void upLoadAnnex() {
        try {
            log.info("开始测试上传附件");
            File file = new File("src/test/resources/计算书版本_附件.png");
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), new FileInputStream(file));
            MultipartFile[] multipartFiles = new MultipartFile[1];
            multipartFiles[0] = multipartFile;
            AttachedFileDTO attachedFileDTO = new AttachedFileDTO();
            attachedFileDTO.setType("ANNEX");
            attachedFileDTO.setTemplateWorkbookId(tempalateWorkbookID);
            attachedFileDTO.setProductMemberId(productMemberId);
            attachedFileDTO.setCreateBy(USERID);
            attachedFileDTO.setReleaseStatus(0);
            Result<?> result = attachedFileService.upLoadAnnex(multipartFiles, attachedFileDTO);
            log.error(result.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 发布计算书模板版本
     * 
     * @author dongbd
     * @date 2021/11/10 09:13
     */
    @Test
    @Order(9)
    public void releaseExcel() {
        log.info("开始测试发布计算书模板版本");
        ReleaseTemplateDTO releaseTemplateDTO = new ReleaseTemplateDTO();
        releaseTemplateDTO.setCreateBy(USERID);
        releaseTemplateDTO.setId(tempalateWorkbookID);
        releaseTemplateDTO.setVersionDescription("版本说明");
        try {
            File file = new File("src/test/resources/计算书版本_导图.png");
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), new FileInputStream(file));
            MultipartFile[] multipartFiles = new MultipartFile[1];
            multipartFiles[0] = multipartFile;
            AttachedFileDTO attachedFileDTO = new AttachedFileDTO();
            attachedFileDTO.setType("IMAGE");
            attachedFileDTO.setTemplateWorkbookId(tempalateWorkbookID);
            attachedFileDTO.setProductMemberId(productMemberId);
            attachedFileDTO.setCreateBy(USERID);
            attachedFileDTO.setReleaseStatus(0);
            Result<?> result = templateWorkbookService.releaseExcel(multipartFiles, releaseTemplateDTO);
            if (result.getCode().equals(200)) {
                log.info("发布计算书模板版本成功");
            } else {
                log.error(result.getMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 通过id查询历史版本详情
     * 
     * @author dongbd
     * @date 2021/11/10 10:09
     */
    @Test
    @Order(10)
    public void queryById() {
        log.info("开始测试查询历史版本详情");
        Result<WorkBookDetailsVO> result = templateWorkbookService.getDetailsById(tempalateWorkbookID);
        if (result.getCode().equals(200)) {
            log.info("查询历史版本详情成功：{}", result.getResult());
        } else {
            log.error(result.getMessage());
        }
    }

    /**
     * 验证计算书模板并生成上下文
     *
     * @author dongbd
     * @date 2021/11/10 10:09
     */
    @Test
    @Order(11)
    public void validateAndCreatContext() {
        log.info("开始测试验证计算书模板并生成上下文");
        Result<?> result = templateWorkbookService.validateAndCreatContext(tempalateWorkbookID, contextId);
        if (result.getCode().equals(200)) {
            log.info("验证计算书模板并生成上下文成功：{}", result.getResult());
        } else {
            log.error(result.getMessage());
        }
    }

    /**
     * 查看计算书版本列表
     * 
     * @author Zhaoyubo
     * @date 2021/11/15 14:43
     * @return void
     */
    @Test
    @Order(101)
    public void queryTemplateWorkbook() {
        log.info("查看计算书版本列表");
        Result<List<TemplateWorkbookVO>> result = templateWorkbookService.queryTemplateWorkbook(productMemberId);
        if (result.getCode().equals(200)) {
            log.info("计算书版本列表展示：{}", result.getResult());
        } else {
            log.error(result.getMessage());
        }
    }

    /**
     * 设置计算书可用状态
     * 
     * @author Zhaoyubo
     * @date 2021/11/15 14:49
     * @return void
     */
    @Test
    @Order(102)
    public void isAvailable() {
        log.info("设置计算书可用状态");
        Result<Object> result = templateWorkbookService.isAvailable(tempalateWorkbookID, 0);
        if (result.getCode().equals(200)) {
            log.info("计算书版本列表展示：{}", result.getResult());
        } else {
            log.error(result.getMessage());
        }
    }

    /**
     * 通过id删除
     * 
     * @author dongbd
     * @date 2021/11/10 10:09
     */
    @Test
    @Order(901)
    public void deleteExcel() {
        log.info("开始测试删除已发布的计算书模板版本");
        Result<?> result = templateWorkbookService.removeWorkFile(tempalateWorkbookID);
        if (result.getCode().equals(200)) {
            log.info("测试删除计算书模板版本成功");
        } else {
            log.error(result.getMessage());
        }
    }

    /**
     * 删除初始化数据
     * 
     * @author dongbd
     * @date 2021/11/10 10:09
     */
    @Test
    @Order(902)
    public void deleteProductMember() {
        log.info("删除辅助数据表：{}", supportService.deleteSheet(supportId).getCode().equals(200) ? "成功" : "失败");
        log.info("删除单体类型数据：{}", productMemberMapper.deleteById(productMemberId) > 0 ? "成功" : "失败");
        log.info("删除单体数据：{}", productMemberMapper.deleteById(monomerId) > 0 ? "成功" : "失败");
        log.info("删除工艺阶段数据：{}", productMemberMapper.deleteById(technologyStageId) > 0 ? "成功" : "失败");
        log.info("删除产品线数据：{}", productMemberMapper.deleteById(productLineId) > 0 ? "成功" : "失败");
    }
}
