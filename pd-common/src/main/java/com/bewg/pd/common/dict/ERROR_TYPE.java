package com.bewg.pd.common.dict;

import java.util.Arrays;
import java.util.Optional;

import com.bewg.pd.common.exception.PdException;

/**
 * 权限错误类型
 * 
 * @author lizy
 */
public enum ERROR_TYPE {

    ACCOUNT_LOCKED("1501", "账号已被锁定,请联系管理员!"), USER_NOT_EXIST("1502", "用户不存在!"), TOKEN_ILLEGAL("1503", "token非法无效!"), TOKEN_EMPTY("1504", "token为空!"), TOKEN_TIEMOUT("1505", "Token失效，请重新登录!"),
    USERNAME_OR_PASSWORE_ERROR("1506", "用户名或者密码不正确!");

    private String _key;
    private String _description;

    ERROR_TYPE(String key, String description) {
        this._key = key;
        this._description = description;
    }

    public String getKey() {
        return this._key;
    }

    public String getDescription() {
        return this._description;
    }

    public static ERROR_TYPE valueOfKey(String key) {
        Optional<ERROR_TYPE> optionalValue = Arrays.stream(values()).filter((item) -> item.getKey().equals(key)).findFirst();
        if (optionalValue.isPresent()) {
            return optionalValue.get();
        } else {
            throw new PdException("Can't find enum ERROR_TYPE for key " + key);
        }
    }

    public static Optional<ERROR_TYPE> optionalValueOfKey(String key) {
        return Arrays.stream(values()).filter((item) -> item.getKey().equals(key)).findFirst();
    }
}
