package com.bewg.pd.common.util;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.bewg.pd.common.exception.PdException;

import lombok.extern.slf4j.Slf4j;

/**
 * 阿里云OSS服务器工具类
 * 
 * @author lizy
 */
@Slf4j
@Component
public class OssUtil {
    @Autowired
    private static OSS ossClient;

    /**
     * ---------变量----------
     */
    @Value("${aliyun.oss.endpoint}")
    private static String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private static String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private static String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    private static String bucketName;

    // 文件存储目录
    private static String filedir = "my_file/";

    /**
     * 1、单个文件上传
     * 
     * @param file
     * @return 返回完整URL地址
     */
    public static String uploadFile(MultipartFile file) {
        String fileUrl = uploadImg2Oss(file);
        String str = getFileUrl(fileUrl);
        return str.trim();
    }

    /**
     * 1、单个文件上传(指定文件名（带后缀）)
     *
     * @param file
     * @return 返回完整URL地址
     */
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            InputStream inputStream = file.getInputStream();
            uploadFile2OSS(inputStream, fileName);
            return fileName;
        } catch (Exception e) {
            return "上传失败";
        }
    }

    /**
     * 2、多文件上传
     *
     * @param fileList
     * @return 返回完整URL，逗号分隔
     */
    public String uploadFile(List<MultipartFile> fileList) {
        String fileUrl = "";
        String str = "";
        String photoUrl = "";
        for (int i = 0; i < fileList.size(); i++) {
            fileUrl = uploadImg2Oss(fileList.get(i));
            str = getFileUrl(fileUrl);
            if (i == 0) {
                photoUrl = str;
            } else {
                photoUrl += "," + str;
            }
        }
        return photoUrl.trim();
    }

    /**
     * 3、通过文件名获取文完整件路径
     *
     * @param fileUrl
     * @return 完整URL路径
     */
    public static String getFileUrl(String fileUrl) {
        if (fileUrl != null && fileUrl.length() > 0) {
            String[] split = fileUrl.split("/");
            String url = getUrl(filedir + split[split.length - 1]);
            return url;
        }
        return null;
    }

    /**
     * 获取去掉参数的完整路径
     */
    private static String getShortUrl(String url) {
        String[] imgUrls = url.split("\\?");
        return imgUrls[0].trim();
    }

    /**
     * 获得url链接
     */
    private static String getUrl(String key) {
        // 设置URL过期时间为20年 3600l* 1000*24*365*20
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 20);
        // 生成URL
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return getShortUrl(url.toString());
        }
        return null;
    }

    /**
     * 上传文件
     */
    private static String uploadImg2Oss(MultipartFile file) {
        // 1、限制最大文件为20M
        if (file.getSize() > 1024 * 1024 * 20) {
            return "图片太大";
        }

        String fileName = file.getOriginalFilename();
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String uuid = UUID.randomUUID().toString();
        String name = uuid + suffix;

        try {
            InputStream inputStream = file.getInputStream();
            uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            return "上传失败";
        }
    }

    /**
     * 上传文件（指定文件名）
     */
    private static String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            // 上传文件
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") || FilenameExtension.equalsIgnoreCase(".jpg") || FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") || FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") || FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        // PDF
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        return "image/jpeg";
    }

    /**
     * @author lizy
     * @desc 下载文件
     * @date 2019-07-31 11:31
     */
    public static void downloadoss(String filename) throws IOException {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, filename);
        // 读取文件内容。
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        FileOutputStream out = new FileOutputStream(filename);
        byte[] buffer = new byte[1024];
        int lenght = 0;
        while ((lenght = in.read(buffer)) != -1) {
            out.write(buffer, 0, lenght);
        }
        if (out != null) {
            out.flush();
            out.close();
        }
        if (in != null) {
            in.close();
        }
    }

    /**
     * 文件下载
     * 
     * @param: fileName
     * @param: outputStream
     * @return: void
     * @create: 2020/10/31 16:19
     * @author: lizy
     */
    public static void download(String fileUrl, String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        // 文件名以附件的形式下载
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        try {
            byte[] buffer = getFileStream(fileUrl);
            ServletOutputStream outputStream = response.getOutputStream();
            // 把输出流放入缓存流
            BufferedOutputStream out = new BufferedOutputStream(outputStream);
            out.write(buffer, 0, buffer.length);

            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            throw new PdException(e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String fileUrl) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        boolean exist = ossClient.doesObjectExist(bucketName, fileUrl);
        if (!exist) {
            throw new PdException("文件不存在,fileUrl={}" + fileUrl);
        }
        log.info("删除文件,filePath={}", fileUrl);
        ossClient.deleteObject(bucketName, fileUrl);
        ossClient.shutdown();
    }

    /**
     * 获取文件缓存流
     */
    public static byte[] getFileStream(String fileUrl) throws IOException {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, fileUrl);
        // 读取文件内容。
        InputStream inputStream = ossObject.getObjectContent();
        // 把输入流放入缓存流
        BufferedInputStream in = new BufferedInputStream(inputStream);

        // byte[] buffer = inputStream.readAllBytes(); //java 11
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in_b = swapStream.toByteArray();
        return in_b;
    }
}
