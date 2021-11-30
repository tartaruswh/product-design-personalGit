package com.bewg.pd.workbook.service;


import com.bewg.pd.common.entity.excel.Sheet;
import com.bewg.pd.common.entity.req.WorkbookCalcReq;
import com.bewg.pd.common.entity.req.WorkbookContextReq;
import com.bewg.pd.common.entity.vo.Result;
import com.grapecity.documents.excel.Workbook;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * excel操作接口
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
public interface IWorkbookService {

    /**
     * 解析excel todo 后期参数需要改成filepath?
     *
     * @param fis
     * @return
     */
    Result parseExcel(Workbook fis);

    /**
     * 获取excel外部引用文件列表 todo 后期参数需要改成filepath?
     *
     * @param fis
     * @return
     */
    Result getRefFileNames(InputStream fis);

    /**
     * 创建计算书上下文实例
     *
     * @param workbookContextReq
     * @return
     */
    Result createContext(WorkbookContextReq workbookContextReq);

    /**
     * 根据入参驱动excel计算
     *
     * @param workbookCalcReq
     * @return
     */
    Result calculate(WorkbookCalcReq workbookCalcReq);

}
