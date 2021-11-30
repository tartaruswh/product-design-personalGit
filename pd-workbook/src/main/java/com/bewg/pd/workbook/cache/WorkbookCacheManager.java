package com.bewg.pd.workbook.cache;

import com.grapecity.documents.excel.Workbook;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 计算书模板缓存管理器
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
public class WorkbookCacheManager {

    /**
     * 计算书缓存对象
     */
    private static Map<String, Workbook> workbookCache = new ConcurrentHashMap<>();

    /**
     * 缓存计算书
     *
     * @param contextId
     * @param workbook
     * @return
     */
    public static synchronized boolean cacheWorkbook(String contextId, Workbook workbook) {
        if (StringUtils.isEmpty(contextId) || workbook == null) {
            return false;
        }
        workbookCache.put(contextId, workbook);
        return true;
    }

    /**
     * 获取计算书缓存
     *
     * @param contextId
     * @return
     */
    public static synchronized Workbook getWorkbookCache(String contextId) {
        if (StringUtils.isEmpty(contextId) || !workbookCache.containsKey(contextId)) {
            return null;
        }
        return workbookCache.get(contextId);
    }

}
