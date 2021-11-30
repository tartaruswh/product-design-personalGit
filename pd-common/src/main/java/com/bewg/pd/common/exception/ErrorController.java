package com.bewg.pd.common.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bewg.pd.common.constant.CommonConstant;
import com.bewg.pd.common.dict.ERROR_TYPE;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(tags = "filter错误处理")
public class ErrorController extends BasicErrorController {
    public ErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        HttpStatus status = getStatus(request);
        String message = (String)body.get("message");
        if (StringUtils.isNotBlank(message)) {
            try {
                ERROR_TYPE errorType = ERROR_TYPE.valueOfKey(message);
                body.put("success", false);
                body.put("code", CommonConstant.AUTH_ERROR_CODE);
                body.put("message", errorType.getDescription());
                body.put("error", errorType.getDescription());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        ResponseEntity<Map<String, Object>> mapResponseEntity = new ResponseEntity<>(body, status);
        return mapResponseEntity;
    }
}