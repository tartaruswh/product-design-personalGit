package com.bewg.pd.common.exception;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.dict.ERROR_TYPE;
import com.bewg.pd.common.entity.vo.Result;
import com.bewg.pd.common.util.ServletUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理器
 * 
 * @author lizy
 * @Date 20211026
 */
@RestControllerAdvice
@Slf4j
public class PdExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(PdException.class)
    public Result handleRRException(PdException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error(404, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Result.error("数据库中已存在该记录");
    }

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public Result handleAuthorizationException(AuthorizationException e) {
        log.error(e.getMessage(), e);
        return Result.noauth("没有权限，请联系管理员授权");
    }

    @ExceptionHandler(ShiroException.class)
    public Result handleShiroException(ShiroException e) {
        Result result = new Result();
        result.setCode(500);
        result.setSuccess(false);
        try {
            ERROR_TYPE errorType = ERROR_TYPE.valueOfKey(e.getMessage());
            if (errorType != null) {
                result.setCode(CommonConstant.AUTH_ERROR_CODE);
                result.setResult(errorType.getDescription());
            }
        } catch (Exception ei) {
            result.setCode(CommonConstant.SC_INTERNAL_SERVER_ERROR_500);
            result.setResult(e.getMessage());
            log.info(ei.getMessage());
        }
        return result;
    }

    /**
     * 参数校验
     * 
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult results = e.getBindingResult();
        String message = "";
        if (results.hasErrors()) {
            message = results.getFieldError().getDefaultMessage();
        }
        log.error(message, e);
        return Result.error(message);
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        if (ex.getClass().getName().endsWith("ClientAbortException")) {
            return null;
        }
        Result result = new Result();
        result.setCode(500);
        result.setSuccess(false);
        if ((ex instanceof TypeMismatchException) || (ex instanceof MethodArgumentTypeMismatchException) || (ex instanceof MethodArgumentNotValidException) || (ex instanceof BindException) || (ex instanceof ConstraintViolationException)) {
            result.setMessage("参数错误");
        } else {
            result.setSuccess(false);
            result.setMessage("系统错误");
        }
        this.logError(request, ex);
        return result;
    }

    private void logError(HttpServletRequest request, Exception ex) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(ex.getMessage());
        buffer.append("\n");
        buffer.append(ServletUtils.getInfo(request));
        log.error(buffer.toString(), ex);
    }

    /**
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result HttpRequestMethodNotSupportedException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error("没有权限，请联系管理员授权");
    }

    /**
     * @param e
     * @return
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    public Result UnsupportedEncodingException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error("编码转换异常！");
    }

}
