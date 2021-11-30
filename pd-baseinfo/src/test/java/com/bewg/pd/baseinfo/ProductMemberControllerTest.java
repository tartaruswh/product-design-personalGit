package com.bewg.pd.baseinfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bewg.pd.baseinfo.modules.entity.ProductMember;
import com.bewg.pd.baseinfo.modules.entity.dto.ProductMemberDTO;
import com.bewg.pd.baseinfo.modules.entity.dto.ProductTreeOrderDTO;
import com.bewg.pd.baseinfo.modules.entity.vo.ProductMemberTree;
import com.bewg.pd.baseinfo.modules.service.IProductMemberService;
import com.bewg.pd.common.entity.vo.Result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = BaseinfoApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductMemberControllerTest {

    private static String currentData;
    private static Long firstId;
    private static Long secondId;

    static {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        currentData = formatter.format(currentTime);
    }

    @Autowired
    private IProductMemberService productMemberService;

    /**
     * 先删除所有数据后再执行测试
     * 
     * @author dongbd
     * @date 2021/10/22 17:22
     */
    @Test
    @Order(1)
    void deleteProductMembers() {
        QueryWrapper<ProductMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id", 0L);
        productMemberService.remove(queryWrapper);

    }

    /**
     * 添加
     * 
     * @author dongbd
     * @date 2021/10/22 14:55
     */
    @Test
    @Order(2)
    void saveProductMember() {
        ProductMemberDTO productMemberDTO1 = new ProductMemberDTO();
        ProductMemberDTO productMemberDTO2 = new ProductMemberDTO();
        productMemberDTO1.setMemberName("产品序列" + currentData + "001");
        productMemberDTO2.setMemberName("产品序列" + currentData + "002");
        productMemberDTO1.setMemberOrder(1);
        productMemberDTO2.setMemberOrder(2);
        productMemberDTO1.setParentId(0L);
        productMemberDTO2.setParentId(0L);
        productMemberDTO1.setParentType("ROOT");
        productMemberDTO2.setParentType("ROOT");
        Result<?> result = productMemberService.saveProductMember(productMemberDTO1);
        Result<?> result1 = productMemberService.saveProductMember(productMemberDTO2);
        log.info(productMemberService.list().toString());
        if (result.getCode() == 200 && result1.getCode() == 200) {
            log.info("测试新增产品成员成功");
        }

    }

    /**
     * 查看
     * 
     * @author dongbd
     * @date 2021/10/22 15:27
     */
    @Test
    @Order(3)
    void queryProductMemberTree() {
        Result<ProductMemberTree> result = productMemberService.queryProductMemberTree(0L, 1);
        firstId = result.getResult().getChildProductMemberList().get(0).getProductMemberVo().getId();
        secondId = result.getResult().getChildProductMemberList().get(1).getProductMemberVo().getId();
        log.info(result.getResult().toString());
        if (result.getCode() == 200) {
            log.info("测试查看产品树成功");
        }
    }

    /**
     * 修改
     * 
     * @author dongbd
     * @date 2021/10/22 15:27
     */
    @Test
    @Order(4)
    void updateProductMember() {
        ProductMemberDTO productMemberDTO = new ProductMemberDTO();
        productMemberDTO.setId(firstId);
        productMemberDTO.setMemberName("产品序列" + currentData + "003");
        Result<?> result = productMemberService.updateProductMember(productMemberDTO);
        log.info(productMemberService.list().toString());
        if (result.getCode() == 200) {
            log.info("测试修改产品成员成功");
        }
    }

    /**
     * 排序
     * 
     * @author dongbd
     * @date 2021/10/22 15:27
     */
    @Test
    @Order(5)
    void editProductTreeOrder() {
        ProductTreeOrderDTO productTreeOrderDTO = new ProductTreeOrderDTO();
        productTreeOrderDTO.setParentId(0L);
        HashMap<Long, Integer> map = new HashMap<>();
        map.put(firstId, 2);
        map.put(secondId, 1);
        productTreeOrderDTO.setOrderMap(map);
        Result<?> result = productMemberService.editProductTreeOrder(productTreeOrderDTO);
        log.info(productMemberService.list().toString());
        if (result.getCode() == 200) {
            log.info("测试调整产品顺序成功");
        }
    }

    /**
     * 删除
     * 
     * @author dongbd
     * @date 2021/10/22 17:22
     */
    @Test
    @Order(6)
    void deleteProductMember() {
        productMemberService.deleteProductMember(firstId);
        Result<?> result = productMemberService.deleteProductMember(secondId);
        log.info(productMemberService.list().toString());
        if (result.getCode() == 200) {
            log.info("测试删除产品成员成功");
        }
    }
}