package com.bewg.pd.baseinfo.modules.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bewg.pd.baseinfo.modules.entity.Support;
import com.bewg.pd.baseinfo.modules.entity.vo.SupportVo;
import com.bewg.pd.common.entity.vo.Result;

/**
 * @Description: 辅助数据表
 * @Author: Zhaoyubo
 * @Date: 2021-10-19
 * @Version: V1.0
 */
public interface ISupportService extends IService<Support> {

    Result supportAdd(MultipartFile file, Long productLineId);

    Result supportUpdate(MultipartFile file, Long id, Long productLineId);

    Result deleteSheet(Long id);

    Result downloadSheet(String excelName, String excelUrl, String fileExtension, HttpServletResponse response) throws UnsupportedEncodingException;

    Result<List<SupportVo>> findSupport(Long productLineId);

    Result<?> uploadSupport(MultipartFile file, String targetName, Long productMemberId);
}
