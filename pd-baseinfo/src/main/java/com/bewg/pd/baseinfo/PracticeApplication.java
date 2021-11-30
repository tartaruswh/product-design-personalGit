package com.bewg.pd.baseinfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author lizy
 */
public class PracticeApplication {
    public static void main(String[] args) {
        List<String> tables = new ArrayList<>();
        tables.add("t_monomer");

        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/workbook", "root", "mty95190").globalConfig(builder -> {
            builder.author("lizy").outputDir(System.getProperty("user.dir") + "/baseinfo/src/main/java") // 输出路径(写到java目录)
                .enableSwagger() // 开启swagger
                .commentDate("yyyy-MM-dd").fileOverride(); // 开启覆盖之前生成的文件

        }).packageConfig(builder -> {
            builder.parent("com.bewg.pd.baseinfo").moduleName("modules").entity("entity").service("service").serviceImpl("serviceImpl").controller("controller").mapper("mapper").xml("mapper")
                .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + "/baseinfo/src/main/java/com/bewg/pd/baseinfo/modules/mapper/xml"));
        }).strategyConfig(builder -> {
            builder.addInclude(tables).addTablePrefix("p_").serviceBuilder().formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl").entityBuilder().enableLombok().logicDeleteColumnName("deleted")
                .enableTableFieldAnnotation().controllerBuilder().formatFileName("%sController").enableRestStyle().mapperBuilder().superClass(BaseMapper.class).formatMapperFileName("%sMapper").enableMapperAnnotation()
                .formatXmlFileName("%sMapper");
        }).templateConfig(builder -> {
            builder.disable(TemplateType.ENTITY).entity("/templates/entity.java").service("/templates/service.java").serviceImpl("/templates/serviceImpl.java").mapper("/templates/mapper.java").mapperXml("/templates/mapper.xml")
                .controller("/templates/controller.java");
        }).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
            .execute();
    }
}
