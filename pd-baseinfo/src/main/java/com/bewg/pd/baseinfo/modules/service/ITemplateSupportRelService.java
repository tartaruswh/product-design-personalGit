package com.bewg.pd.baseinfo.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.TemplateSupportRel;

import java.util.List;

/**
 * 计算书模板和辅助数据关系表
 *
 * @author dongbd
 * @date 2021-11-09 15:58
 **/
public interface ITemplateSupportRelService extends IService<TemplateSupportRel> {
    List<Support> getSupportsByTempalteId(Long tempalteWorkbookId);

    void deleteByTemplateId(Long templateId);
}
