package com.bewg.pd.workbook.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 属性赋值组件
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Slf4j
public class DozerMapper {

    private static final ConcurrentHashMap<String, Mapper> mapperMap = new ConcurrentHashMap<>();

    private static final List<String> mappingFiles = new ArrayList<>();

    private DozerMapper() {
        throw new RuntimeException("");
    }

    /**
     * 加载配置文件 created by YinHF on 2020-03-23
     */
    static {
        configMappingFile("dozer-mapping.xml");
    }

    /**
     * 获取Mapper.
     *
     * @param sourceClass
     *            源类
     * @param destClass
     *            目标类
     * @return Mapper
     */
    public static Mapper getMapper(Class sourceClass, Class destClass) {
        // throw new RuntimeException("2");
        String mapperKey = Joiner.on(">>").join(sourceClass.getName(), destClass.getName());
        if (mapperMap.containsKey(mapperKey)) {
            return mapperMap.get(mapperKey);
        } else {
            // spring上下文获取Mapper
            Mapper mapper = getFromSpring(sourceClass, destClass);
            if (mapper == null) {
                mapper = creatMaper(sourceClass, destClass);
            }
            mapperMap.put(mapperKey, mapper);
            return mapper;
        }
    }

    /**
     * 从上下文获取.
     *
     * @param sourceClass
     *            源类
     * @param destClass
     *            目标类.
     */
    private static Mapper getFromSpring(Class sourceClass, Class destClass) {
        try {
            return (Mapper)ApplicationContextUtil.getBean(Joiner.on("_").join(sourceClass.getSimpleName(), destClass.getSimpleName(), "Mapper"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建Mapper.
     *
     * @param sourceClass
     *            源类
     * @param destClass
     *            目标类
     * @return Mapper
     */
    private static Mapper creatMaper(Class sourceClass, Class destClass) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = DozerMapper.class.getClassLoader();
        }

        return DozerBeanMapperBuilder.create().withMappingBuilder(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(type(sourceClass).mapEmptyString(true).mapNull(false), type(destClass), TypeMappingOptions.wildcardCaseInsensitive(true));
            }
        }).withClassLoader(classLoader).withMappingFiles(mappingFiles).build();
    }

    /**
     * 构造新的destinationClass实例对象，通过source对象中的字段内容. 映射到destinationClass实例对象中，并返回新的destinationClass实例对象。
     *
     * @param source
     *            源数据对象
     * @param destinationClass
     *            要构造新的实例对象Class
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        // 判空
        if (source == null) {
            return null;
        }
        return getMapper(source.getClass(), destinationClass).map(source, destinationClass);
    }

    /**
     * 单个转换 允许指定映射id
     *
     * @param mapId
     *            映射id，见dozer-mapping.xml created by YinHF on 2020-09-11
     */
    public static <T> T map(Object source, Class<T> destinationClass, String mapId) {
        // 判空
        if (source == null) {
            return null;
        }
        return getMapper(source.getClass(), destinationClass).map(source, destinationClass, mapId);
    }

    /**
     * 拷贝,但是忽略null值的属性.
     *
     * @param source
     *            源对象
     * @param destination
     *            目标对象
     */
    public static void copySkipNull(Object source, Object destination) {
        getMapper(source.getClass(), destination.getClass()).map(source, destination);
    }

    /**
     * 批处理,处理源集合到目标集合的数据转换.
     *
     * @param sourceList
     *            源对象集合
     * @param destinationClass
     *            目标类集合,
     * @param <T>
     *            泛型,任意类的类型
     * @return 返回转换后的对象
     */
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        // 判空
        if (sourceList == null) {
            return null;
        }

        List destinationList = Lists.newArrayList();
        for (Iterator iterator = sourceList.iterator(); iterator.hasNext();) {
            Object sourceObject = iterator.next();
            Object destinationObject = map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 批量转换 允许指定映射id
     *
     * @param sourceList
     *            源列表
     * @param destinationClass
     *            目标类字节码
     * @param <T>
     *            目标类泛型
     * @param mapId
     *            映射id，见dozer-mapping.xml
     * @return created by YinHF on 2020-09-11
     */
    public static <S, T> List<T> mapList(Collection<S> sourceList, Class<T> destinationClass, String mapId) {
        // 判空
        if (sourceList == null) {
            return null;
        }

        List destinationList = Lists.newArrayList();
        for (Iterator iterator = sourceList.iterator(); iterator.hasNext();) {
            Object sourceObject = iterator.next();
            Object destinationObject = map(sourceObject, destinationClass, mapId);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 将对象source的所有属性值拷贝到对象destination中.
     *
     * @param source
     *            对象source
     * @param destination
     *            对象destination
     */
    public static void copy(Object source, Object destination) {
        copySkipNull(source, destination);
    }

    /**
     * 将对象source的所有属性值拷贝到对象destination中. 如果dozer报错则使用 spring的 BeanUtils.copyProperties
     *
     * @param source
     *            对象source
     * @param destination
     *            对象destination
     */
    public static void copyOrSpringCopy(Object source, Object destination) {
        try {
            copySkipNull(source, destination);
        } catch (Exception e) {
            log.error("[DozerMapper copy error]");
            BeanUtils.copyProperties(source, destination);
        }
    }

    public static void configMappingFile(String mappingFile) {
        if (!mappingFiles.contains(mappingFile)) {
            mappingFiles.add(mappingFile);
        }
    }

    public static void configMappingFile(List<String> mappingFiles) {
        mappingFiles.forEach(DozerMapper::configMappingFile);
    }
}
