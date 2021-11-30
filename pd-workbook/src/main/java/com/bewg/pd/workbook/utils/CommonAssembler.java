package com.bewg.pd.workbook.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bewg.pd.common.enums.BaseTypeEnum;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 对象组装器
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Slf4j
public class CommonAssembler {

    private CommonAssembler() {
        throw new IllegalAccessError("这是一个装配器");
    }

    /**
     * 模型转视图实体(Ipage batch version)
     *
     * @param pageList
     * @return
     */
    public static <T, E> IPage<T> fromPage(IPage<E> pageList, Class<T> clazz) {
        IPage<T> pageListResp = new Page<>(pageList.getCurrent(), pageList.getPages());
        pageListResp.setRecords(new ArrayList<>());
        DozerMapper.copySkipNull(pageList, pageListResp);
        pageListResp.setRecords(fromList(pageList.getRecords(), clazz));
        return pageListResp;
    }

    /**
     * 模型转视图实体(batch version)
     *
     * @param modelList
     * @return
     */
    public static <T, E> List<T> fromList(List<E> modelList, Class<T> clazz) {
        List<T> respList = Lists.newArrayList();
        for (E model : modelList) {
            respList.add(fromSingle(model, clazz));
        }
        return respList;
    }

    /**
     * 模型转视图实体(single version)
     *
     * @param model
     * @return
     */
    public static <T, E> T fromSingle(E model, Class<T> clazz) {
        return DozerMapper.map(model, clazz);
    }

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for (Field field : ReflectUtil.getAllFields(obj)) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = null;
                value = field.get(obj);
                if (value instanceof Date) {
                    if (StringUtils.isNotBlank(value.toString())) {
                        map.put(fieldName, sdf.format((Date)value));
                    } else {
                        map.put(fieldName, "");
                    }
                }
                // 枚举
                else if (value instanceof BaseTypeEnum) {
                    map.put(fieldName, ((BaseTypeEnum)value).getDesc());
                } else {
                    if (value == null) {
                        map.put(fieldName, "");
                    } else {
                        map.put(fieldName, value);
                    }

                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
}
