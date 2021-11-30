package com.bewg.pd.common.util;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 自定义List分页工具
 * 
 * @author lizy
 */
public class PageUtils {

    /**
     * 获取分页对象
     * 
     * @param list
     * @param pageNo
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> IPage<T> getPage(List<T> list, Integer pageNo, Integer pageSize) {
        int totalCount = list.size();
        IPage<T> page = new Page<>();
        page.setTotal(totalCount);
        page.setCurrent(pageNo);
        int pageCount = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        page.setPages(pageCount);
        list = startPage(list, pageNo, pageSize);
        page.setRecords(list);
        return page;
    }

    /**
     * 开始分页
     * 
     * @param list
     * @param pageNo
     *            页码
     * @param pageSize
     *            每页多少条数据
     * @return
     */
    public static <T> List<T> startPage(List<T> list, Integer pageNo, Integer pageSize) {
        if (list == null || list.size() == 0) {
            return list;
        }
        Integer count = list.size(); // 记录总数
        Integer pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        if (pageNo <= 0) {
            pageNo = 1;
        }
        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引
        if (pageNo < pageCount) {
            fromIndex = (pageNo - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else if (pageNo.equals(pageCount)) {
            fromIndex = (pageNo - 1) * pageSize;
            toIndex = count;
        } else {
            fromIndex = (pageCount - 1) * pageSize;
            toIndex = count;
        }
        List pageList = list.subList(fromIndex, toIndex);
        return pageList;
    }
}
