package com.bewg.pd.common.entity.excel;

import com.bewg.pd.common.entity.req.WorkbookContextReq;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计算书上下文对象
 * </p>
 *
 * @author tianzhitao
 * @since 2021-11-03
 */
@Data
public class WorkbookContext implements Serializable {

    /**
     * 上下文编号
     */
    private String contextId;

    /**
     * 单体编号
     */
    private String productMemberId;

    /**
     * 模型文档路径(文件服务器上的计算书文件)
     */
    private String docPath;

    /**
     * 模型文档中文名称
     */
    private String docName;

    /**
     * 关联文档路径
     */
    private List<WorkbookContextReq.ParamPair> associatedDocs;

    /**
     * 工作目录
     */
    private String workDir;

    /**
     * 本地模型文档路径
     */
    private String localDocPath;

    /**
     * 本地模型文档引用外部文件路径
     */
    private List<String> localDocLinkPath;

    /**
     * 上次输入的必须输入参数
     */
    private Map<String, Object> requiredParams;

    /**
     * 上次输入的谨慎调整参数
     */
    private Map<String, Object> cautiousParams;

    /**
     * 上次输入的谨慎调整参数公式项
     */
    private Map<String, Object> cautiousFormulaParams;

    /**
     * 计算书解析后数据结构
     */
    private Sheet calcResult;

}
