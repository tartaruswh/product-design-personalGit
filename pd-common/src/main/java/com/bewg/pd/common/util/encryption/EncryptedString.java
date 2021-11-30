package com.bewg.pd.common.util.encryption;

import lombok.Data;

@Data
public class EncryptedString {

    public static final String key = "1234567890adbcde";// 长度为16个字符

    public static final String iv = "1234567890hjlkew";// 长度为16个字符
}
