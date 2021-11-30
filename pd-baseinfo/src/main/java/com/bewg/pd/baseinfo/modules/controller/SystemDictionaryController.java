package com.bewg.pd.baseinfo.modules.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bewg.pd.baseinfo.modules.entity.IdsReqParameter;
import com.bewg.pd.baseinfo.modules.entity.SystemDictionary;
import com.bewg.pd.baseinfo.modules.entity.vo.SystemDictionaryVO;
import com.bewg.pd.baseinfo.modules.service.ISystemDictionaryService;
import com.bewg.pd.common.aspect.annotation.AutoLog;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.util.IdWorkerUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 系统字典
 * @Author:
 * @Date: 2021-11-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "系统字典")
@RestController
@RequestMapping("/systemDictionary")
public class SystemDictionaryController {
    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    /**
     * 添加
     * 
     * @param systemDictionary
     * @return
     */
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping(value = "/add")
    public Result<List<SystemDictionary>> add(@RequestBody SystemDictionary systemDictionary) {
        Result<List<SystemDictionary>> result = new Result<>();
        IdWorkerUtil idWorkerUtil = new IdWorkerUtil();
        systemDictionary.setId(idWorkerUtil.nextId());
        int addCount = systemDictionaryService.add(systemDictionary);
        if (addCount > 0) {
            return getListResult(systemDictionary, result, CommonConstant.OPERATE_TYPE_2);
        } else {
            throw new PdException("添加失败");
        }

    }

    /**
     * 编辑
     * 
     * @param systemDictionary
     * @return
     */
    @AutoLog(value = "编辑")
    @ApiOperation(value = "编辑", notes = "编辑")
    @PutMapping
    public Result<List<SystemDictionary>> edit(@RequestBody SystemDictionary systemDictionary) {
        Result<List<SystemDictionary>> result = new Result<>();
        int addCount = systemDictionaryService.edit(systemDictionary);
        if (addCount > 0) {
            return getListResult(systemDictionary, result, CommonConstant.OPERATE_TYPE_3);
        } else {
            throw new PdException("更新失败");
        }
    }

    private Result<List<SystemDictionary>> getListResult(@RequestBody SystemDictionary systemDictionary, Result<List<SystemDictionary>> result, int operate) {
        if (null == systemDictionary.getParentId() || systemDictionary.getParentId() == 0) {
            return systemDictionaryService.queryLevelOne("");
        }
        if (systemDictionary.getParentId() > 0) {
            result.setCode(200);
            result.setSuccess(true);
            if (CommonConstant.OPERATE_TYPE_2 == operate) {
                result.setMessage("添加成功");
            }
            if (CommonConstant.OPERATE_TYPE_3 == operate) {
                result.setMessage("更新成功");
            }
            result.setResult(null);
        }
        return result;
    }

    /**
     * 通过id删除
     * 
     * @param id
     * @return
     */
    @AutoLog(value = "通过id删除")
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable(name = "id") String id) {
        Result result = new Result();
        if (systemDictionaryService.delete(id) > 0) {
            result.setCode(200);
            result.setSuccess(true);
            result.setMessage("删除成功");
        } else {
            throw new PdException("删除失败");
        }

        return result;
    }

    /**
     * 批量删除
     * 
     * @param idsReqParameter
     * @return
     */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @DeleteMapping(value = "/batch")
    public Result<SystemDictionary> deleteBatch(@RequestBody IdsReqParameter idsReqParameter) {
        Result<SystemDictionary> result = new Result<>();
        if (idsReqParameter.getIds().length > 0) {
            this.systemDictionaryService.removeByIds(Arrays.asList(idsReqParameter.getIds()));
            result.setCode(200);
            result.setSuccess(true);
            result.setMessage("删除成功!");
        } else {
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("参数无法识别！");
        }
        return result;
    }

    /**
     * 通过id查询
     * 
     * @param id
     * @return
     */
    @AutoLog(value = "通过id查询")
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping(value = "/{id}")
    public Result<SystemDictionary> queryById(@PathVariable(name = "id") String id) {
        Result<SystemDictionary> result = new Result<>();
        SystemDictionary systemDictionary = systemDictionaryService.getById(id);
        if (systemDictionary == null) {
            result.error500("未找到对应系统字典");
        } else {
            result.setResult(systemDictionary);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 查询一级字典信息
     *
     * @param id
     * @return
     */
    @AutoLog(value = "查询一级字典信息")
    @ApiOperation(value = "查询一级字典信息", notes = "查询一级字典信息")
    @GetMapping(value = "/levelone")
    public Result<List<SystemDictionary>> queryLevelOne(@PathVariable(name = "id", required = false) String id) {
        return systemDictionaryService.queryLevelOne(id);
    }

    /**
     * 获取所有层级字典树
     *
     * @param id
     * @return
     */
    @AutoLog(value = "获取所有层级字典树")
    @ApiOperation(value = "获取所有层级字典树", notes = "获取所有层级字典树")
    @GetMapping(value = "/queryLevels")
    public Result<List<SystemDictionaryVO>> queryLevels(@RequestParam(name = "id", required = false) String id) {
        return systemDictionaryService.queryLevels(id);
    }

    /**
     * 分页列表查询二级字典
     * 
     * @param id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "分页列表查询二级字典")
    @ApiOperation(value = "分页列表查询二级字典", notes = "分页列表查询二级字典")
    @GetMapping(value = "/list")
    public Result<IPage<SystemDictionary>> queryPageList(@RequestParam(name = "id") String id, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return systemDictionaryService.queryPageList(id, pageNo, pageSize);
    }

    /**
     * 根据字典名称模糊查询
     *
     * @param id
     * @param keyWord
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "根据字典名称模糊查询")
    @ApiOperation(value = "根据字典名称模糊查询", notes = "根据字典名称模糊查询")
    @GetMapping(value = "/keyword")
    public Result<IPage<SystemDictionary>> queryByKeyword(@RequestParam(name = "id") String id, @RequestParam(name = "keyWord") String keyWord, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return systemDictionaryService.queryByKeyword(id, keyWord, pageNo, pageSize);
    }

    /**
     * 根据code查询
     *
     * @param code
     * @return
     */
    @AutoLog(value = "根据code查询")
    @ApiOperation(value = "根据code查询", notes = "根据code查询")
    @GetMapping(value = "/code")
    public Result<List<SystemDictionaryVO>> queryByKeyCode(@RequestParam(name = "code") String code) {
        return systemDictionaryService.queryByKeyCode(code);
    }
}
