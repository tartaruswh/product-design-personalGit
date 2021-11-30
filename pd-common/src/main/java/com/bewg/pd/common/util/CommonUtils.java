package com.bewg.pd.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.exception.PdException;
import com.bewg.pd.common.util.filter.FileTypeFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lizy
 */
@Slf4j
public class CommonUtils {

    /**
     * 中文正则
     */
    private static Pattern ZHONGWEN_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 判断文件名是否带盘符，重新处理
     * 
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName) {
        // 判断是否带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1) {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        // 替换上传文件名字的特殊字符
        fileName = fileName.replace("=", "").replace(",", "").replace("&", "").replace("#", "").replace("“", "").replace("”", "");
        // 替换上传文件名字中的空格
        fileName = fileName.replaceAll("\\s", "");
        return fileName;
    }

    /**
     * java 判断字符串里是否包含中文字符
     */
    public static boolean ifContainChinese(String str) {
        if (str.getBytes().length == str.length()) {
            return false;
        } else {
            Matcher m = ZHONGWEN_PATTERN.matcher(str);
            if (m.find()) {
                return true;
            }
            return false;
        }
    }

    /**
     * 统一全局上传
     *
     * @Return: java.lang.String
     */
    public static String upload(MultipartFile file, String uploadType) {
        String reFileUrl = "";
        // fastdfs 方式文件存储
        if (CommonConstant.UPLOAD_TYPE_FASTDFS.equals(uploadType)) {
            reFileUrl = FastdfsUtil.uploadfastdfs(file);
        }
        // 阿里oss 方式文件存储
        if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
            reFileUrl = OssUtil.uploadFile(file);
        }
        return reFileUrl;
    }

    /**
     * 统一全局下载
     *
     * @Return: java.lang.String
     */
    public static void download(String fileName, String fileUrl, String fileExtension, String uploadType, HttpServletResponse response) throws UnsupportedEncodingException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");
        // 不同类型的文件对应不同的MIME类型
        // response.setContentType("application/octet-stream");
        response.setContentType("application/json");
        // 对文件名进行编码处理中文问题
        fileName = new String(fileName.getBytes(), StandardCharsets.UTF_8);
        // inline在浏览器中直接显示，不提示用户下载
        // attachment弹出对话框，提示用户进行下载保存本地
        // 默认为inline方式
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + fileExtension);
        // fastdfs 方式文件存储
        if (CommonConstant.UPLOAD_TYPE_FASTDFS.equals(uploadType)) {
            FastdfsUtil.download(fileName, fileUrl, response);
        }
        // 阿里oss 方式文件存储
        if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
            OssUtil.download(fileUrl, fileName, response);
        }
    }

    /**
     * 本地文件上传
     *
     * @param mf
     *            文件
     * @param bizPath
     *            自定义路径
     * @return
     */
    public static String uploadLocal(MultipartFile mf, String bizPath, String uploadpath) {
        try {
            // 过滤上传文件类型
            FileTypeFilter.fileTypeFilter(mf);
            // 过滤上传文件类型
            String fileName = null;
            File file = new File(uploadpath + File.separator + bizPath + File.separator);
            if (!file.exists()) {
                // 创建文件根目录
                file.mkdirs();
            }
            // 获取文件名
            String orgName = mf.getOriginalFilename();
            orgName = CommonUtils.getFileName(orgName);
            if (orgName.indexOf(".") != -1) {
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."));
            } else {
                fileName = orgName + "_" + System.currentTimeMillis();
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = null;
            if (oConvertUtils.isNotEmpty(bizPath)) {
                dbpath = bizPath + File.separator + fileName;
            } else {
                dbpath = fileName;
            }
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            return dbpath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 把文件打成压缩包下载
     */
    public static void downloadZipFiles(HttpServletResponse response, List<String> filesUrl, String zipFileName, String uploadType) {
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            // 不同类型的文件对应不同的MIME类型
            response.setContentType("application/x-msdownload");
            // 对文件名进行编码处理中文问题
            zipFileName = new String(zipFileName.getBytes(), StandardCharsets.UTF_8);
            // inline在浏览器中直接显示，不提示用户下载
            // attachment弹出对话框，提示用户进行下载保存本地
            // 默认为inline方式
            response.setHeader("Content-Disposition", "attachment;filename=" + zipFileName);
            // --设置成这样可以不用保存在本地，再输出， 通过response流输出,直接输出到客户端浏览器中。
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            zipFile(filesUrl, zipFileName, zos, uploadType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件
     *
     * @param filePaths
     *            需要压缩的文件路径集合
     * @throws IOException
     */
    private static void zipFile(List<String> filePaths, String zipFileName, ZipOutputStream zos, String uploadType) {
        // 设置读取数据缓存大小
        byte[] buffer = null;
        try {
            // 循环读取文件路径集合，获取每一个文件的路径
            for (String fileUrl : filePaths) {
                // fastdfs 方式文件存储
                if (CommonConstant.UPLOAD_TYPE_FASTDFS.equals(uploadType)) {
                    // 创建输入流读取文件
                    buffer = FastdfsUtil.getFileStream(fileUrl);
                }
                // 阿里oss 方式文件存储
                if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
                    // 创建输入流读取文件
                    buffer = OssUtil.getFileStream(fileUrl);
                }
                // 将文件写入zip内，即将文件进行打包
                zos.putNextEntry(new ZipEntry(zipFileName));
                // 写入文件的方法，同上
                int size = 0;
                // 设置读取数据缓存大小
                zos.write(buffer, 0, buffer.length);
                // 关闭输入输出流
                zos.closeEntry();
            }
        } catch (IOException e) {
            throw new PdException(e.getMessage());
        } finally {
            if (null != zos) {
                try {
                    zos.close();
                } catch (IOException e) {
                    throw new PdException(e.getMessage());
                }
            }
        }
    }

    /**
     * 统一文件
     *
     * @Return: java.lang.String
     */
    public static void deleteFile(String fileUrl, String uploadType) throws UnsupportedEncodingException {
        // fastdfs 方式文件存储
        if (CommonConstant.UPLOAD_TYPE_FASTDFS.equals(uploadType)) {
            FastdfsUtil.deleteFile(fileUrl);
        }
        // 阿里oss 方式文件存储
        if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
            OssUtil.deleteFile(fileUrl);
        }
    }

    /** 当前系统数据库类型 */
    private static String DB_TYPE = "";
    private static DbType dbTypeEnum = null;

    /**
     * 全局获取平台数据库类型（对应mybaisPlus枚举）
     *
     * @return
     */
    public static DbType getDatabaseTypeEnum() {
        if (oConvertUtils.isNotEmpty(dbTypeEnum)) {
            return dbTypeEnum;
        }
        try {
            DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
            dbTypeEnum = JdbcUtils.getDbType(dataSource.getConnection().getMetaData().getURL());
            return dbTypeEnum;
        } catch (SQLException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据数据源key获取DataSourceProperty
     *
     * @param sourceKey
     * @return
     */
    public static DataSourceProperty getDataSourceProperty(String sourceKey) {
        DynamicDataSourceProperties prop = SpringContextUtils.getApplicationContext().getBean(DynamicDataSourceProperties.class);
        Map<String, DataSourceProperty> map = prop.getDatasource();
        DataSourceProperty db = (DataSourceProperty)map.get(sourceKey);
        return db;
    }

    /**
     * 根据sourceKey 获取数据源连接
     *
     * @param sourceKey
     * @return
     * @throws SQLException
     */
    public static Connection getDataSourceConnect(String sourceKey) throws SQLException {
        if (oConvertUtils.isEmpty(sourceKey)) {
            sourceKey = "master";
        }
        DynamicDataSourceProperties prop = SpringContextUtils.getApplicationContext().getBean(DynamicDataSourceProperties.class);
        Map<String, DataSourceProperty> map = prop.getDatasource();
        DataSourceProperty db = (DataSourceProperty)map.get(sourceKey);
        if (db == null) {
            return null;
        }
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(db.getDriverClassName());
        ds.setUrl(db.getUrl());
        ds.setUsername(db.getUsername());
        ds.setPassword(db.getPassword());
        return ds.getConnection();
    }

}