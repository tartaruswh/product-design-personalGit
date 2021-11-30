package com.bewg.pd.baseinfo.modules.mapper;

import com.bewg.pd.baseinfo.modules.entity.Support;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bewg.pd.baseinfo.modules.entity.TemplateSupportRel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 辅助数据表和计算书模板关联关系表
 *
 * @author dongbd
 * @date 2021-11-09 15:58
 **/
@Mapper
public interface TemplateSupportRelMapper extends BaseMapper<TemplateSupportRel> {

    /**
     * 根据辅助数据表ID查找关联表中计算书ID
     *
     * @author Zhaoyubo
     * @date 2021/11/11 14:09
     * @param id
     *            辅助数据表ID
     * @return java.util.List<java.lang.Long>
     */
    @Select("SELECT template_id FROM t_template_support_rel t WHERE t.`support_id`=#{id}")
    List<Long> findTemplateId(Long id);

    @Select("SELECT s.excel_name,  s.excel_url, s.file_extension FROM t_support_workbook s " + " LEFT JOIN t_template_support_rel rel on s.id = rel.support_id " + " LEFT JOIN t_template_workbook w on rel.template_id = w.id "
        + " WHERE w.id = #{tempalteWorkbookId} AND s.is_del = 0 AND w.is_del = 0 AND rel.is_del = 0")
    List<Support> getSupportsByTempalteId(@Param("tempalteWorkbookId") Long tempalteWorkbookId);
}
