package com.bewg.pd.baseinfo.modules.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.apache.ibatis.annotations.Param;
import com.bewg.pd.baseinfo.modules.entity.TemplateWorkbook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计算书模板
 *
 * @author dongbd
 * @date 2021-11-01 17:37
 **/
@Mapper
public interface TemplateWorkbookMapper extends BaseMapper<TemplateWorkbook> {

    @Select("SELECT count(*) FROM t_template_workbook t WHERE t.`version`=#{version}")
    int findRefNumber(@Param("version") String version);
}
