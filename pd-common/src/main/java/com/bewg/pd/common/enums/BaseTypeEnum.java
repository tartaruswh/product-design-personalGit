package com.bewg.pd.common.enums;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 枚举模板
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22common
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface BaseTypeEnum<T> extends Serializable {

    String getName();

    T getCode();

    String getDesc();

}