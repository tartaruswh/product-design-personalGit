package com.bewg.pd.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author lizy
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor cpuThreadPool() {
        /*系统多线程用于计算,属于cpu密集型*/
        int coreCount = Runtime.getRuntime().availableProcessors() + 1;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数目
        executor.setCorePoolSize(coreCount);
        // 指定最大线程数
        executor.setMaxPoolSize(coreCount * 2);
        // 队列中最大的数目
        executor.setQueueCapacity(64);
        // 线程名称前缀
        executor.setThreadNamePrefix("cpuThreadPool_");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        // 加载
        executor.initialize();
        return executor;
    }
}
