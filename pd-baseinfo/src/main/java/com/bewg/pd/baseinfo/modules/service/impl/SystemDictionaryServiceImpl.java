package com.bewg.pd.baseinfo.modules.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewg.pd.baseinfo.modules.entity.SystemDictionary;
import com.bewg.pd.baseinfo.modules.entity.vo.SystemDictionaryVO;
import com.bewg.pd.baseinfo.modules.mapper.SystemDictionaryMapper;
import com.bewg.pd.baseinfo.modules.service.ISystemDictionaryService;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.util.IdWorkerUtil;
import com.bewg.pd.common.util.OrikaUtils;

/**
 * @Description: 系统字典
 * @Author: lizy
 * @Date: 2021-11-09
 * @Version: V1.0
 */
@Service
public class SystemDictionaryServiceImpl extends ServiceImpl<SystemDictionaryMapper, SystemDictionary> implements ISystemDictionaryService {

    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;

    @Override
    public int add(SystemDictionary systemDictionary) {
        IdWorkerUtil idWorkerUtil = new IdWorkerUtil();
        systemDictionary.setId(idWorkerUtil.nextId());
        boolean isExist;
        if (null == systemDictionary.getParentId() || systemDictionary.getParentId() == 0) {
            isExist = isExistOfName(systemDictionary.getId(), (long)0, systemDictionary.getDicName());
        } else {
            isExist = isExistOfName(systemDictionary.getId(), systemDictionary.getParentId(), systemDictionary.getDicName());
        }
        if (isExist) {
            throw new PdException("字典名称已经存在！");
        } else {
            return systemDictionaryMapper.insert(systemDictionary);
        }
    }

    @Override
    public int edit(SystemDictionary systemDictionary) {
        boolean isExist;
        SystemDictionary systemDictionaryEntity = systemDictionaryMapper.selectById(systemDictionary.getId());
        if (systemDictionaryEntity == null) {
            throw new PdException("未找到对应系统字典！");
        } else {
            if (null == systemDictionary.getParentId() || systemDictionary.getParentId() == 0) {
                isExist = isExistOfName(systemDictionary.getId(), (long)0, systemDictionary.getDicName());
            } else {
                isExist = isExistOfName(systemDictionary.getId(), systemDictionary.getParentId(), systemDictionary.getDicName());
            }
            if (isExist) {
                throw new PdException("字典名称已经存在！");
            } else {
                return systemDictionaryMapper.updateById(systemDictionary);
            }
        }
    }

    @Override
    public int delete(String id) {
        // 是否存在子集
        List<SystemDictionaryVO> list = getChilds(Long.parseLong(id));
        if (null != list && list.size() > 0) {
            throw new PdException("存在子集，不能删除");
        } else {
            return systemDictionaryMapper.deleteById(id);
        }
    }

    @Override
    public Result<List<SystemDictionary>> queryLevelOne(String id) {
        // 返回结果
        Result<List<SystemDictionary>> result = new Result<>();
        // 获取根结点（）
        QueryWrapper<SystemDictionary> queryWrapper = new QueryWrapper();
        if (null != id && !"".equals(id)) {
            queryWrapper.eq("parent_id", id);
        } else {
            queryWrapper.eq("parent_id", 0);
        }
        // 获取根结点list
        List<SystemDictionary> rootSystemDictionary = systemDictionaryMapper.selectList(queryWrapper);
        result.setCode(200);
        result.setSuccess(true);
        result.setResult(rootSystemDictionary);
        return result;
    }

    @Override
    public Result<List<SystemDictionaryVO>> queryLevels(String id) {
        // 返回结果
        Result<List<SystemDictionaryVO>> result = new Result<>();
        // 结果集合
        List<SystemDictionaryVO> resultListVo = new ArrayList<>();
        // 获取根结点（）
        QueryWrapper<SystemDictionary> queryWrapper = new QueryWrapper();
        if (null != id && !"".equals(id)) {
            queryWrapper.eq("id", id);
        } else {
            queryWrapper.eq("parent_id", 0);
        }
        resultListVo = getTreeData(queryWrapper);
        result.setCode(200);
        result.setResult(resultListVo);
        return result;
    }

    @Override
    public Result<IPage<SystemDictionary>> queryPageList(String id, Integer pageNo, Integer pageSize) {
        Result<IPage<SystemDictionary>> result = new Result<>();
        QueryWrapper<SystemDictionary> queryWrapper = new QueryWrapper();
        if (null != id && !"".equals(id)) {
            queryWrapper.eq("parent_id", id);
        }
        Page<SystemDictionary> page = new Page<>(pageNo, pageSize);
        IPage<SystemDictionary> pageList = systemDictionaryMapper.selectPage(page, queryWrapper);
        result.setCode(200);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 递归获取子集
     *
     * @return
     */
    private List<SystemDictionaryVO> getChilds(Long id) {
        // 结果集合
        List<SystemDictionaryVO> resultListVo = new ArrayList<>();
        // 获取节点列表
        QueryWrapper<SystemDictionary> queryWrapper = new QueryWrapper();
        if (null != id && !"".equals(id)) {
            queryWrapper.eq("parent_id", id);
            resultListVo = getTreeData(queryWrapper);
        }
        return resultListVo;
    }

    @Override
    public Result<IPage<SystemDictionary>> queryByKeyword(String id, String keyWord, Integer pageNo, Integer pageSize) {
        Result<IPage<SystemDictionary>> result = new Result<>();
        QueryWrapper<SystemDictionary> queryWrapper = new QueryWrapper();
        if (null != id && !"".equals(id)) {
            queryWrapper.eq("parent_id", id);
        }
        if (!StringUtils.isBlank(keyWord)) {
            queryWrapper.like("dic_name", keyWord);
        }
        queryWrapper.orderByDesc("create_time");
        Page<SystemDictionary> page = new Page<>(pageNo, pageSize);
        IPage<SystemDictionary> pageList = systemDictionaryMapper.selectPage(page, queryWrapper);
        result.setCode(200);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @Override
    public Result<List<SystemDictionaryVO>> queryByKeyCode(String code) {
        // 返回结果
        Result<List<SystemDictionaryVO>> result = new Result<>();
        // 结果集合
        List<SystemDictionaryVO> resultListVo;
        // 获取根结点（）
        QueryWrapper<SystemDictionary> queryWrapper = new QueryWrapper();
        if (null != code && !"".equals(code)) {
            queryWrapper.eq("dic_code", code);
        } else {
            throw new PdException("编码不能为空");
        }
        resultListVo = getTreeData(queryWrapper);
        result.setCode(200);
        result.setResult(resultListVo);
        return result;
    }

    private List<SystemDictionaryVO> getTreeData(QueryWrapper queryWrapper) {
        // 结果集合
        List<SystemDictionaryVO> resultListVo = new ArrayList<>();
        // 获取根结点list
        List<SystemDictionary> rootList = systemDictionaryMapper.selectList(queryWrapper);
        // 遍历字典层级
        if (null != rootList && rootList.size() > 0) {
            for (SystemDictionary systemDictionary : rootList) {
                SystemDictionaryVO systemDictionaryVO = OrikaUtils.convert(systemDictionary, SystemDictionaryVO.class);
                // 获取子集
                systemDictionaryVO.setChileDictionary(getChilds(systemDictionaryVO.getId()));
                resultListVo.add(systemDictionaryVO);
            }
        }
        return resultListVo;
    }

    /**
     * 检查名称是否存在
     *
     * @param Id
     * @param parentId
     * @param memberName
     * @return
     */
    private boolean isExistOfName(Long Id, Long parentId, String memberName) {
        boolean result = false;
        QueryWrapper<SystemDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dic_name", memberName).eq("parent_id", parentId).ne("id", Id);
        List<SystemDictionary> list = systemDictionaryMapper.selectList(queryWrapper);
        if (null != list && list.size() > 0) {
            result = true;
        }
        return result;
    }
}
