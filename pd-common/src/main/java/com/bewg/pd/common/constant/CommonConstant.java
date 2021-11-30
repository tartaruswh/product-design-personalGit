package com.bewg.pd.common.constant;

/**
 * @author lizy
 */
public interface CommonConstant {

    /**
     * 系统日志类型： 登录
     */
    int LOG_TYPE_1 = 1;

    /**
     * 系统日志类型： 操作
     */
    int LOG_TYPE_2 = 2;

    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    Integer SC_OK_200 = 200;

    /**
     * 访问权限认证未通过 510
     */
    Integer SC_WORKBOOK_NO_AUTHZ = 510;

    /**
     * token失效 505
     */
    Integer TOKEN_TIME_OUT = 505;

    /**
     * 权限错误返回码
     */
    Integer AUTH_ERROR_CODE = 701;
    /**
     * 微服务读取配置文件属性 服务地址
     */
    public final static String CLOUD_SERVER_KEY = "spring.cloud.nacos.discovery.server-addr";

    /**
     * 登录用户拥有角色缓存KEY前缀
     */
    String LOGIN_USER_CACHERULES_ROLE = "loginUser_cacheRules::Roles_";
    /**
     * 登录用户拥有权限缓存KEY前缀
     */
    String LOGIN_USER_CACHERULES_PERMISSION = "loginUser_cacheRules::Permissions_";
    /**
     * 登录用户令牌缓存KEY前缀
     */
    int TOKEN_EXPIRE_TIME = 3600;
    /**
     * token前缀
     */
    String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN_";
    /**
     * token后缀
     */
    String SUFFIX_USER_TOKEN = "_SUFFIX_USER_TOKEN_";
    /**
     * 用户操作信息
     */
    String PREFIX_USER_OPS = "PREFIX_USER_OPS";
    String SUFFIX_USER_OPS = "SUFFIX_USER_OPS";

    /**
     * 操作日志类型： 查询
     */
    int OPERATE_TYPE_1 = 1;

    /**
     * 操作日志类型： 添加
     */
    int OPERATE_TYPE_2 = 2;

    /**
     * 操作日志类型： 更新
     */
    int OPERATE_TYPE_3 = 3;

    /**
     * 操作日志类型： 删除
     */
    int OPERATE_TYPE_4 = 4;

    /**
     * 操作日志类型： 导入
     */
    int OPERATE_TYPE_5 = 5;

    /**
     * 操作日志类型： 导出
     */
    int OPERATE_TYPE_6 = 6;

    /**
     * 字典翻译文本后缀
     */
    String DICT_TEXT_SUFFIX = "_dictText";
    /**
     * 消息类型1:通知公告2:系统消息
     */
    public static final String MSG_CATEGORY_1 = "1";
    public static final String MSG_CATEGORY_2 = "2";

    /**
     * 枚举类所在包
     */
    String DICT_PACKAGE = "com.bewg.pd.common.modules.common.dict";
    /**
     * 
     */
    String X_ACCESS_TOKEN = "Authorization";

    /**
     * 文件上传类型（fastdfs：fastdfs，Minio：minio，阿里云：alioss）
     */
    public static final String UPLOAD_TYPE_FASTDFS = "fastdfs";
    public static final String UPLOAD_TYPE_MINIO = "minio";
    public static final String UPLOAD_TYPE_OSS = "alioss";

}
