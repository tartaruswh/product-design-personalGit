package com.bewg.pd.common.dict;

import java.util.Arrays;
import java.util.Optional;

import com.bewg.pd.common.exception.PdException;

/**
 * 是否删除
 * 
 * @author lizy
 */
public enum DEL_FLAG {

    DELETED("1", "已删除"), NOT_DELETED("0", "正常");

    private String _key;
    private String _description;

    DEL_FLAG(String key, String description) {
        this._key = key;
        this._description = description;
    }

    public String getKey() {
        return this._key;
    }

    public String getDescription() {
        return this._description;
    }

    public static com.bewg.pd.common.dict.DEL_FLAG valueOfKey(String key) {
        Optional<DEL_FLAG> optionalValue = Arrays.stream(values()).filter((item) -> item.getKey().equals(key)).findFirst();
        if (optionalValue.isPresent()) {
            return optionalValue.get();
        } else {
            throw new PdException("Can't find enum DEL_FLAG for key " + key);
        }
    }

    public static Optional<DEL_FLAG> optionalValueOfKey(String key) {
        return Arrays.stream(values()).filter((item) -> item.getKey().equals(key)).findFirst();
    }
}
