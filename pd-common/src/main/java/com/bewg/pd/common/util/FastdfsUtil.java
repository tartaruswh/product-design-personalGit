package com.bewg.pd.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.bewg.pd.common.exception.PdException;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lizy
 */
@Slf4j
@Component
public class FastdfsUtil {

    @Autowired
    private FastFileStorageClient client;
    private static FastdfsUtil fastdfsUtil;

    @PostConstruct
    public void init() {
        fastdfsUtil = this;
    }

    /**
     * fastdfs 存储方式 上传
     *
     * @param file
     * @return
     */
    public static String uploadfastdfs(MultipartFile file) {
        StorePath storePath = null;
        try {
            storePath = fastdfsUtil.client.uploadFile(file.getInputStream(), file.getSize(), FileUtil.extName(file.getOriginalFilename()), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 服务器路径+存储路径
        String supportUrl = storePath == null ? null : storePath.getFullPath();
        System.out.println(supportUrl);
        return supportUrl;

    }

    /**
     * 统一全局下载
     *
     * @Return: java.lang.String
     */
    public static void download(String fileName, String fileUrl, HttpServletResponse response) {
        // 分离文件分组
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        // 分离文件路径
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        // 进行文件下载
        byte[] buffer = fastdfsUtil.client.downloadFile(group, path, new DownloadByteArray());

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.write(buffer, outputStream);
        } catch (IOException e) {
            throw new PdException(e.getMessage());
        }
    }

    /**
     * 根据下载路径获取输入流
     *
     * @param fileUrl
     * @return
     */
    public static InputStream getFileInputStream(String fileUrl) {
        // 分离文件分组
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        // 分离文件路径
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        // 进行文件下载
        byte[] buffer = fastdfsUtil.client.downloadFile(group, path, new DownloadByteArray());

        return new ByteArrayInputStream(buffer);
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String fileUrl) {
        // 分离文件分组
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        // 分离文件路径
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        // 进行文件删除
        fastdfsUtil.client.deleteFile(group, path);
    }

    /**
     * 获取文件缓存流
     */
    public static byte[] getFileStream(String fileUrl) {

        // 分离文件分组
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        // 分离文件路径
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        // 进行文件下载
        byte[] buffer = fastdfsUtil.client.downloadFile(group, path, new DownloadByteArray());

        return buffer;
    }
}
