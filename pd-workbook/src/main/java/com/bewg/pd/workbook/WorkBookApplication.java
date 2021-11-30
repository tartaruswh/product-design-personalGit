package com.bewg.pd.workbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务启动类
 * </p>
 *
 * @author tianzhitao
 * @since 2021-10-22
 */
@Slf4j
@SpringBootApplication
public class WorkBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkBookApplication.class, args);
    }
}
