package com.bewg.pd.common.system.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DictModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public DictModel() {}

    public DictModel(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 字典value
     */
    private Integer value;
    /**
     * 字典文本
     */
    private String text;

    /**
     * 特殊用途： JgEditableTable
     * 
     * @return
     */
    public String getTitle() {
        return this.text;
    }

}
