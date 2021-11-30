package com.bewg.pd.baseinfo.modules.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.TemplateSupportRel;
import com.bewg.pd.baseinfo.modules.mapper.TemplateSupportRelMapper;
import com.bewg.pd.baseinfo.modules.service.ITemplateSupportRelService;

/**
 * 计算书模板和辅助数据关系表
 *
 * @author dongbd
 * @date 2021-11-09 15:58
 **/
@Service
public class TemplateSupportRelServiceImpl extends ServiceImpl<TemplateSupportRelMapper, TemplateSupportRel> implements ITemplateSupportRelService {

    @Autowired
    private TemplateSupportRelMapper templateSupportRelMapper;

    /**
     * 通过计算书模板Id获取所有关联辅助数据表
     *
     * @author dongbd
     * @date 2021/11/11 13:27
     * @param tempalteWorkbookId
     *            计算书模板Id
     * @return java.util.List<com.bewg.pd.baseinfo.modules.entity.Support>
     */
    @Override
    public List<Support> getSupportsByTempalteId(Long tempalteWorkbookId) {
        return templateSupportRelMapper.getSupportsByTempalteId(tempalteWorkbookId);
    }

    @Override
    public void deleteByTemplateId(Long templateId) {
        QueryWrapper<TemplateSupportRel> templateSupportRelQueryWrapper = new QueryWrapper<>();
        templateSupportRelQueryWrapper.eq("template_id", templateId);
        templateSupportRelMapper.delete(templateSupportRelQueryWrapper);
    }
}
