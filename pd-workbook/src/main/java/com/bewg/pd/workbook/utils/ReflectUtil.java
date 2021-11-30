package com.bewg.pd.workbook.utils;

import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 反射工具类
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
public class ReflectUtil {
    /**
     * 固定实例，仅用于判断映射中是否存在某个key
     */
    private final static Object PRESENT = new Object();

    /**
     * 驼峰转换为下划线
     *
     * @param camelCaseName
     *            userName ->user_name
     * @return
     */
    public static String underLineName(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 转换为驼峰 user_name -> userName
     *
     * @param underscoreName
     * @return
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        // 入参判空
        if (object == null) {
            return new Field[0];
        }

        // 调用更一般的方法
        return getAllFields(object.getClass(), true, false);
    }

    /**
     * 获取类的所有属性
     *
     * @param clazz
     *            类字节码
     * @param includingSuper
     *            是否包含父类的属性
     * @param excludingOverridden
     *            是否排除子类覆盖父类的属性，只有includingSuper为true时才有意义
     * @return created by YinHF on 2020-08-03
     */
    public static Field[] getAllFields(Class<?> clazz, boolean includingSuper, boolean excludingOverridden) {
        // 获取类本身的属性
        Field[] fields = clazz.getDeclaredFields();

        // 如果不要求包含父类的属性，则直接返回
        if (!includingSuper) {
            return fields;
        }

        // 声明结果列表
        List<Field> resultList = new ArrayList<>(Arrays.asList(fields));

        // 对结果列表构造映射 {属性名称 → 固定实例}
        Map<String, Object> resultMap = new HashMap<>();
        resultList.forEach(result -> resultMap.put(result.getName(), PRESENT));

        // 重置字节码为它的父类
        clazz = clazz.getSuperclass();

        // 循环获取其各级父类的属性
        while (clazz != null) {
            // 获取当前类的所有属性
            Field[] currentFields = clazz.getDeclaredFields();
            ArrayList<Field> subResultList = new ArrayList<>(Arrays.asList(currentFields));

            // 遍历这些属性，添加到reultMap中
            for (Field field : subResultList) {
                // 如果结果集中已存在
                if (resultMap.get(field.getName()) != null) {
                    // 判断是否需要排除
                    if (!excludingOverridden) {
                        resultList.add(field);
                    }
                    // 结束本轮循环
                    continue;
                }

                // 添加到resultMap中
                resultMap.put(field.getName(), PRESENT);

                // 归并
                resultList.add(field);
            }

            // 重置字节码
            clazz = clazz.getSuperclass();
        }

        // 返回
        return resultList.toArray(new Field[0]);
    }

    /**
     * 获取类的指定属性
     *
     * @param clazz
     *            【必填】类字节码
     * @param includingSuper
     *            【必填】是否包含父类的属性
     * @param fieldName
     *            【必填】属性名称
     * @return 属性实例 created by YinHF on 2021-04-28
     */
    public static Field getField(Class<?> clazz, boolean includingSuper, String fieldName) {
        // 获取类本身的属性
        Field[] fields = clazz.getDeclaredFields();

        // 遍历属性，进行匹配
        for (Field field : fields) {
            if (StringUtils.equals(fieldName, field.getName())) {
                return field;
            }
        }

        // 如果不包含父类属性，则方法结束
        if (!includingSuper) {
            return null;
        }

        // 重置字节码为它的父类
        clazz = clazz.getSuperclass();

        // 父类判空
        if (clazz != null) {
            // 递归获取父类属性
            return getField(clazz, true, fieldName);
        } else {
            // 返回空
            return null;
        }
    }

    public static Field getFieldByName(Field[] fields, String filedName) {
        for (Field field : fields) {
            if (filedName.equals(field.getName())) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    public static String getString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }
}
