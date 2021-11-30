package com.bewg.pd.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * 单数据源配置
 *
 * @author lizy
 */
@Configuration
@MapperScan(value = {"com.bewg.pd.baseinfo.modules.mapper", "com.bewg.pd.common.**.mapper"})
public class MybatisPlusConfig {
    /**
     * 分页插件，最新版
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));

        return interceptor;
    }
}
