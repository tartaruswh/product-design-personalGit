package com.bewg.pd.common.validator;

import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lizy
 */
@Slf4j
public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {
    private boolean required = false;

    private Class enumClass = null;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        required = constraintAnnotation.required();
        enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            if (s == null) {
                return true;
            }
            if ("".equals(s)) {
                return false;
            }
            if (enumClass == null) {
                return false;
            } else {
                try {
                    Method method = enumClass.getDeclaredMethod("valueOfKey", String.class);
                    method.invoke(enumClass, s);
                } catch (Exception e) {
                    log.info(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }
}
