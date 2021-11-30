package com.bewg.pd.baseinfo.modules.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bewg.pd.baseinfo.modules.entity.SystemDictionary;
import com.bewg.pd.baseinfo.modules.entity.vo.SystemDictionaryVO;
import com.bewg.pd.common.entity.vo.Result;

/**
 * @Description: 系统字典
 * @Author: lizy
 * @Date: 2021-11-09
 * @Version: V1.0
 */
public interface ISystemDictionaryService extends IService<SystemDictionary> {

    /**
     * 添加字典
     *
     * @param systemDictionary
     * @return
     */
    int add(SystemDictionary systemDictionary);

    /**
     * 编辑字典
     *
     * @param systemDictionary
     * @return
     */
    int edit(SystemDictionary systemDictionary);

    /**
     * 删除字典
     *
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 通过父级id查询
     *
     * @param id
     * @return
     */
    Result<List<SystemDictionary>> queryLevelOne(String id);

    /**
     * 通过id查询字典树
     *
     * @param id
     * @return
     */
    Result<List<SystemDictionaryVO>> queryLevels(String id);

    /**
     * 分页列表查询二级字典
     *
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    Result<IPage<SystemDictionary>> queryPageList(String id, Integer pageNo, Integer pageSize);

    /**
     * 关键字查询
     *
     * @param id
     * @param pageNo
     * @param keyWord
     * @param pageSize
     * @return
     */
    Result<IPage<SystemDictionary>> queryByKeyword(String id, String keyWord, Integer pageNo, Integer pageSize);

    /**
     * 关键字查询
     *
     * @param code
     * @return
     */
    Result<List<SystemDictionaryVO>> queryByKeyCode(String code);
}