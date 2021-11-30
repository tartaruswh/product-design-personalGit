package com.bewg.pd.common.util.filter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bewg.pd.common.exception.PdException;

/**
 * @Description:
 * @author: lizy
 * @date: 2021年10月28日 15:29
 */
public class FileTypeFilter {

    /**
     * 文件后缀
     */
    private static String[] forbidType = {"jsp", "php"};

    /**
     * 初始化文件头类型，不够的自行补充
     */

    final static HashMap<String, String> fileTypeMap = new HashMap<>();

    static {

    }

    /**
     * @param fileName
     * @return String
     * @description 通过文件后缀名获取文件类型
     */
    private static String getFileTypeBySuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    /**
     * 文件类型过滤
     *
     * @param file
     */
    public static void fileTypeFilter(MultipartFile file) throws Exception {
        String suffix = getFileType(file);
        for (String type : forbidType) {
            if (type.contains(suffix)) {
                throw new PdException("上传失败，文件类型异常：" + suffix);
            }
        }
    }

    /**
     * 通过读取文件头部获得文件类型
     *
     * @param file
     * @return 文件类型
     * @throws Exception
     */
    private static String getFileType(MultipartFile file) throws Exception {
        String fileExtendName = null;
        InputStream is;
        try {
            // is = new FileInputStream(file);
            is = file.getInputStream();
            byte[] b = new byte[10];
            is.read(b, 0, b.length);
            String fileTypeHex = String.valueOf(bytesToHexString(b));
            Iterator<String> keyIter = fileTypeMap.keySet().iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                // 验证前5个字符比较
                if (key.toLowerCase().startsWith(fileTypeHex.toLowerCase().substring(0, 5)) || fileTypeHex.toLowerCase().substring(0, 5).startsWith(key.toLowerCase())) {
                    fileExtendName = fileTypeMap.get(key);
                    break;
                }
            }
            // 如果不是上述类型，则判断扩展名
            if (StringUtils.isBlank(fileExtendName)) {
                String fileName = file.getOriginalFilename();
                return getFileTypeBySuffix(fileName);
            }
            is.close();
            return fileExtendName;
        } catch (Exception exception) {
            throw new Exception(exception.getMessage(), exception);
        }
    }

    /**
     * 获得文件头部字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        return getString(src, stringBuilder);
    }

    public static String getString(byte[] src, StringBuilder stringBuilder) {
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
