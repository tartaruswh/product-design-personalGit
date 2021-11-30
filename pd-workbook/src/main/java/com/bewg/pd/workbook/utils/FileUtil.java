package com.bewg.pd.workbook.utils;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * <p>
 * 文件操作工具类
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Slf4j
public class FileUtil {

    public static final String LINUX_SEPARATOR = "/";

    /**
     * 根据文件路径获取文件名(带文件类型)
     *
     * @param fileUrl http://pani-dev.bewgcloud.net.cn/files/2020/11/24/1WZDivCg40SbRgd3Azx88kh7GbJeZv.JPG
     * @return
     */
    public static String getFileNameWithSuffix(String fileUrl) {

        if (StringUtils.isEmpty(fileUrl)) {
            return "";
        }

        List<String> partList = Splitter.on("/").splitToList(fileUrl);

        if (partList.size() == 0) {
            return "";
        }

        return partList.get(partList.size() - 1);
    }

    /**
     * 获取文件后缀
     *
     * @param filePath
     * @return
     */
    public static String getFileSuffix(String filePath) {

        String fileName = getFileNameWithSuffix(filePath);

        if (fileName == null || "".equals(fileName)) {
            return null;
        }

        List<String> segments = Splitter.on(".").splitToList(fileName);

        if (segments.size() == 0) {
            return null;
        }

        return segments.get(segments.size() - 1);

    }

    /**
     * 根据文件路径获取文件名(不带文件类型)
     *
     * @param fileUrl http://pani-dev.bewgcloud.net.cn/files/2020/11/24/1WZDivCg40SbRgd3Azx88kh7GbJeZv.JPG
     * @return
     */
    public static String getFileNameWithoutSuffix(String fileUrl) {

        String fileName = getFileNameWithSuffix(fileUrl);

        if (StringUtils.isEmpty(fileName)) {
            return "";
        }

        List<String> partList = Splitter.on(".").splitToList(fileName);

        if (partList.size() == 0) {
            return "";
        }

        return partList.get(0);
    }

    /**
     * 创建目录
     *
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {

        File dir = new File(destDirName);
        if (dir.exists()) {
            log.error("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            log.info("创建目录" + destDirName + "成功！");
            return true;
        } else {
            log.error("创建目录" + destDirName + "失败！");
            return false;
        }
    }

}
