package com.bewg.pd.common.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bewg.pd.common.exception.PdException;

/**
 * 参数校验工具类
 * 
 * @author dongbd
 * @date 2021/10/28 15:48
 **/
public class ParamCheckUtil {
    private static final String SUFFIX = "不能为空";

    /**
     * 判断Object是否不为空
     * 
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param value
     *            Object
     * @return boolean
     */
    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }

    /**
     * 判断String是否不为空
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param value
     *            String
     * @return boolean
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * 判断Long是否不为空且不为0
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param value
     *            Long
     * @return boolean
     */
    public static boolean isNotEmpty(Long value) {
        return !isEmpty(value);
    }

    /**
     * 判断Object是否为空
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param value
     *            Object
     * @return boolean
     */
    public static boolean isEmpty(Object value) {
        return null == value;
    }

    /**
     * 判断String是否为空
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param value
     *            String
     * @return boolean
     */
    public static boolean isEmpty(String value) {
        return null == value || "".equals(value.trim());
    }

    /**
     * 判断Long是否为空或为0
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param value
     *            Long
     * @return boolean
     */
    public static boolean isEmpty(Long value) {
        return null == value || 0L == value;
    }

    /**
     * 判断集合是否为空
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param values
     *            List
     * @return boolean
     */
    public static boolean isEmpty(List<?> values) {
        return null == values || values.isEmpty();
    }

    /**
     * 校验Object是否为空且不为0
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param exceptionMessage
     *            参数名称
     * @param value
     *            参数值
     */
    public static void checkEmpty(String exceptionMessage, Object value) {
        if (null == value) {
            throwException(exceptionMessage);
        } else if (value instanceof Long && 0L == (Long)value) {
            throwException(exceptionMessage);
        } else if (value instanceof Integer && 0 == (Integer)value) {
            throwException(exceptionMessage);
        } else if (value instanceof Float && 0.0F == (Float)value) {
            throwException(exceptionMessage);
        } else if (value instanceof Double && 0.0D == (Double)value) {
            throwException(exceptionMessage);
        }
    }

    /**
     * 用于校验数值不能为空但可为0
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param exceptionMessage
     *            参数名称
     * @param value
     *            参数值
     */
    public static void checkEmptyWithZero(String exceptionMessage, Object value) {
        if (null == value) {
            throwException(exceptionMessage);
        }
    }

    /**
     * 校验可变参数是否为空且不为0
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param exceptionMessage
     *            参数名称
     * @param values
     *            参数值
     */
    public static void checkEmpty(String exceptionMessage, Object... values) {
        if (null == values) {
            throwException(exceptionMessage);
        } else {
            for (Object value : values) {
                checkEmpty(exceptionMessage, value);
            }
        }
    }

    /**
     * 校验String是否为空
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param exceptionMessage
     *            参数名称
     * @param value
     *            参数值
     */
    public static void checkEmpty(String exceptionMessage, String value) {
        if (StringUtils.isBlank(value)) {
            throwException(exceptionMessage);
        }
    }

    /**
     * 校验Long是否为空且不为0
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param exceptionMessage
     *            参数名称
     * @param value
     *            参数值
     */
    public static void checkEmpty(String exceptionMessage, Long value) {
        if (null == value || 0L == value) {
            throwException(exceptionMessage);
        }
    }

    /**
     * 校验失败抛出自定义异常
     *
     * @author dongbd
     * @date 2021/10/28 17:26
     * @param exceptionMessage
     *            参数名称
     */
    private static void throwException(String exceptionMessage) {
        throw new PdException(exceptionMessage + SUFFIX);
    }

    /**
     * 判断文件大小
     *
     * @param len
     *            文件大小
     * @param size
     *            限制大小
     * @param unit
     *            限制单位（B,K,M,G）
     */
    public static void checkFileSize(long len, int size, String unit) {
        double fileSize = 0;
        switch (unit.toUpperCase()) {
            case "B":
                fileSize = (double)len;
                break;
            case "K":
                fileSize = (double)len / 1024;
                break;
            case "M":
                fileSize = (double)len / 1048576;
                break;
            case "G":
                fileSize = (double)len / 1073741824;
                break;
            default:
                break;
        }
        if (fileSize > size) {
            throw new PdException("文件大小不得大于" + size + unit);
        }
    }

    /**
     * 判断文件大小
     *
     * @param file
     *            文件
     */
    public static void checkEmptyFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new PdException("文件不能为空");
        }
        if (StringUtils.isBlank(file.getOriginalFilename()) && StringUtils.isBlank(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))) && 0 == file.getOriginalFilename().lastIndexOf(".")) {
            throw new PdException("文件名称不能为空");
        }
    }

    /**
     * 判断文件大小
     *
     * @param files
     *            文件集合
     */
    public static void checkEmptyFiles(MultipartFile[] files) {
        if (files == null) {
            throw new PdException("文件不能为空");
        }
        for (MultipartFile file : files) {
            checkEmptyFile(file);
        }
    }
}
