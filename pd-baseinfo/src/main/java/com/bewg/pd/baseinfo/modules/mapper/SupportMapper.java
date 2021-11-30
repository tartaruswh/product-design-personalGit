package com.bewg.pd.baseinfo.modules.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bewg.pd.baseinfo.modules.entity.Support;

/**
 * @Description: 辅助数据表
 * @Author: Zhaoyubo
 * @Date: 2021-10-19
 * @Version: V1.0
 */
@Mapper
public interface SupportMapper extends BaseMapper<Support> {

    /**
     * 被引用次数
     * 
     * @author Zhaoyubo
     * @date 2021/10/25 16:55
     * @param id
     * @return int
     */
    @Select("SELECT count(*) FROM t_template_support_rel t WHERE t.`support_id`=#{id}")
    int findRefNumber(@Param("id") Long id);
}
