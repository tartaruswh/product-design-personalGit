package com.bewg.pd.baseinfo;

/**
 * @author Zhaoyubo
 * @date 2021/10/26
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.vo.SupportVo;
import com.bewg.pd.baseinfo.modules.mapper.SupportMapper;
import com.bewg.pd.baseinfo.modules.service.impl.SupportServiceImpl;
import com.bewg.pd.common.entity.vo.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 辅助数据表测试类
 * 
 * @author Zhaoyubo
 * @date 2021/10/26 11:22
 **/

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BaseinfoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SupportTest {

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private static Long id;

    @Autowired
    private SupportServiceImpl supportService;

    @Autowired
    private SupportMapper supportMapper;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();
    }

    /**
     * 先删除数据再进行测试
     * 
     * @author Zhaoyubo
     * @date 2021/10/26 14:23
     * @return void
     */
    @Test
    @Order(2)
    public void deleteTest() {
        QueryWrapper<Support> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("excel_name", "辅助_测试表");
        queryWrapper.eq("product_line_id", 1L);
        Support support = supportMapper.selectOne(queryWrapper);
        if (support != null) {
            Long id = support.getId();
            supportService.deleteSheet(id);
        }
    }

    /**
     * @author Zhaoyubo
     * @description 新增辅助数据表
     * @date 2021/10/26
     */
    @Test
    @Order(3)
    public void saveSheet() throws IOException {
        File file = new File("src/test/resources/辅助_测试表.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        Result result = supportService.supportAdd(multipartFile, 1L);
        log.info(supportService.list().toString());
        if (result.getCode() == 200) {
            log.info("测试新增辅助数据表成功");
        }
    }

    /**
     * 查看辅助数据表树状结构
     * 
     * @author Zhaoyubo
     * @date 2021/10/26 14:39
     * @return void
     */
    @Test
    @Order(4)
    public void findSheet() {
        QueryWrapper<Support> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("excel_name", "辅助_测试表");
        queryWrapper.eq("product_line_id", 1L);
        Support support = supportMapper.selectOne(queryWrapper);
        id = support.getId();
        Result<List<SupportVo>> result = supportService.findSupport(1L);
        if (result != null) {
            id = result.getResult().get(0).getId();
            System.out.println(id);
            for (int i = 0; i < result.getResult().size(); i++) {
                SupportVo supportVo = result.getResult().get(i);
                System.out.println(supportVo);
            }
            log.info(result.getResult().toString());
            if (result.getCode() == 200) {
                log.info("测试查看辅助数据表树状结构成功");
            }
        }
    }

    /**
     * 文件下载
     */
    @Test
    @Order(5)
    public void downloadSheet() throws UnsupportedEncodingException {
        Support support = supportMapper.selectById(id);
        String excelUrl = support.getExcelUrl();
        String fileExtension = support.getFileExtension();
        /*Result result = */supportService.downloadSheet("测试表", excelUrl, fileExtension, response);
        log.info(supportService.list().toString());
        /*if (result.getCode() == 200) {
            log.info("测试下载辅助数据表成功");
        }*/
    }

    /**
     * 更新辅助数据表
     */
    @Test
    @Order(6)
    public void updateSheet() throws IOException {
        File file = new File("src/test/resources/辅助_测试表.xlsx");
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        Result result = supportService.supportUpdate(multipartFile, id, 1L);
        log.info(supportService.list().toString());
        if (result.getCode() == 200) {
            log.info("测试更新辅助数据表成功");
        }
    }

    /**
     * 根据产品线id获得该id下所有的辅助数据表名称
     */
    @Test
    @Order(7)
    public void excelName() {
        List<String> excelNames = supportService.getExcelNames(1L);
        for (int i = 0; i < excelNames.size(); i++) {
            String name = excelNames.get(i);
            System.out.println(name);
        }
    }

    /**
     * 文件删除
     */
    @Test
    @Order(8)
    public void deleteSheet() {
        Result result = supportService.deleteSheet(id);
        log.info(supportService.list().toString());
        if (result.getCode() == 200) {
            log.info("测试删除辅助数据表成功");
        }
    }

}
